package com.mtech.recycler.service.Impl;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.entity.RecycleCategory;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.model.PricingRequest;
import com.mtech.recycler.model.PricingResponse;
import com.mtech.recycler.model.RecycleResponse;
import com.mtech.recycler.model.SubmitRequest;
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
        List<PricingResponse.Items> items = new ArrayList<>();

        BigDecimal totalPrice = request.getCategories().stream().map(c -> {
            BigDecimal price = recycleCategoryRepository.findByName(c.getName()).getPrice();
            BigDecimal eachItemTotalPrice = price.multiply(new BigDecimal(c.getQuantity()));
            items.add(new PricingResponse.Items(c.getName(), c.getQuantity(), price, eachItemTotalPrice));
            return eachItemTotalPrice;
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("RequestService - GetRequestTotalPricing - total price before promo: %s".formatted(totalPrice));

        if (StringUtils.hasText(request.getPromoCode())) {
            Promotion promotion = promotionRepository.findDiscountByPromotionCode(request.getPromoCode())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonConstant.ErrorMessage.INVALID_PROMOTION_CODE));

            if (!isWithinRange(promotion.getStartDate(), promotion.getEndDate())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, CommonConstant.ErrorMessage.EXPIRED_PROMOTION_CODE);
            }

            totalPrice = totalPrice.add(totalPrice.multiply(BigDecimal.valueOf(promotion.getPercentage()))).setScale(2, RoundingMode.CEILING);
        }

        log.info("RequestService - GetRequestTotalPricing - total price after promo: %s".formatted(totalPrice));

        response.setTotalPrice(totalPrice);
        response.setItems(items);

        log.info("RequestService - GetRequestTotalPricing - end");

        return Optional.of(response);
    }

    @Override
    public List<RecycleCategory> GetAllRecycleCategories() {
        List<RecycleCategory> recycleCategories = new ArrayList<>();
        recycleCategoryRepository.findAll().forEach(recycleCategories::add);

        return recycleCategories;
    }

    @Override
    public Optional<RecycleResponse> SubmitRequest(SubmitRequest request) {
        RecycleRequest recycleRequest = new RecycleRequest();
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
