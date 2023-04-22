package com.mtech.recycler.service.Impl;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.entity.RecycleCategory;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.model.*;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.repository.RecycleRequestRepository;
import com.mtech.recycler.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    final private RecycleCategoryRepository recycleCategoryRepository;
    final private PromotionRepository promotionRepository;

    final private RecycleRequestRepository recycleRequestRepository;

    public RequestServiceImpl(RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository, RecycleRequestRepository recycleRequestRepository) {
        this.recycleCategoryRepository = recycleCategoryRepository;
        this.promotionRepository = promotionRepository;
        this.recycleRequestRepository = recycleRequestRepository;
    }

    @Override
    public Optional<PricingResponse> GetRequestTotalPricing(PricingRequest request) {
        log.info("RequestService - GetRequestTotalPricing - start");
        var response = new PricingResponse();
        List<Item> items = new ArrayList<>();

        BigDecimal totalPrice = CalculateTotalPrice(request.getData(), request.getPromoCode(), items);

        log.info("RequestService - GetRequestTotalPricing - total price after promo: %s".formatted(totalPrice));

        response.setTotalPrice(totalPrice);
        response.setItems(items);

        log.info("RequestService - GetRequestTotalPricing - end");

        return Optional.of(response);
    }

    private BigDecimal CalculateTotalPrice(List<Category> categories, String promoCode, List<Item> items) {
        BigDecimal totalPrice = categories.stream().map(c -> {
            BigDecimal unitPrice = recycleCategoryRepository.findByName(c.getCategory())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The following category name (%s) is not found".formatted(c.getCategory())))
                    .getPrice();

            BigDecimal subTotalPrice = unitPrice.multiply(BigDecimal.valueOf(c.getQuantity()));
            items.add(new Item(c.getCategory(), c.getQuantity(), unitPrice, subTotalPrice));
            return subTotalPrice;
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("RequestService - GetRequestTotalPricing - total price before promo: %s".formatted(totalPrice));

        if (StringUtils.hasText(promoCode)) {
            Promotion promotion = promotionRepository.findDiscountByPromotionCode(promoCode)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonConstant.ErrorMessage.INVALID_PROMOTION_CODE));

            if (!isWithinRange(promotion.getStartDate(), promotion.getEndDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonConstant.ErrorMessage.EXPIRED_PROMOTION_CODE);
            }

            totalPrice = totalPrice.add(totalPrice.multiply(BigDecimal.valueOf(promotion.getPercentage()))).setScale(2, RoundingMode.CEILING);
        }
        return totalPrice;
    }

    @Override
    public List<Category> GetAllRecycleCategories() {
        return StreamSupport.stream(recycleCategoryRepository.findAll().spliterator(), false).map(r -> new Category(r.getName(), r.getPrice(), 0, r.getUnitOfMeasurement())).toList();
    }

    @Override
    public Optional<RecycleResponse> SubmitRequest(SubmitRequest request) {
        com.mtech.recycler.entity.RecycleRequest recycleRequest = new com.mtech.recycler.entity.RecycleRequest();
        recycleRequest.setContactNumber(request.getContactNumber());
        recycleRequest.setContactPerson(request.getContactPerson());
        recycleRequestRepository.save(recycleRequest);
        return Optional.empty();
        //check if username exists database and request not pending
        //save to database
    }

    boolean isWithinRange(Date startDate, Date endDate) {
        Date today = Instant.now().toDate();

        return today.after(startDate) && today.before(endDate);
    }
}
