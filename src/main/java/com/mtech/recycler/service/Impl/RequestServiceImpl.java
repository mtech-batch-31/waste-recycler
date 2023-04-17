package com.mtech.recycler.service.Impl;

import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.entity.RecycleCategory;
import com.mtech.recycler.model.PricingRequest;
import com.mtech.recycler.model.PricingResponse;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    final private RecycleCategoryRepository recycleCategoryRepository;
    final private PromotionRepository promotionRepository;

    public RequestServiceImpl(RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository) {
        this.recycleCategoryRepository = recycleCategoryRepository;
        this.promotionRepository = promotionRepository;
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

        if (StringUtils.hasText(request.getPromoCode())) {
            Promotion promotion = promotionRepository.findDiscountByPromotionCode(request.getPromoCode()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid discount code"));

            if (!isWithinRange(promotion.getStartDate(), promotion.getEndDate())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Your discount code is expired");
            }

            totalPrice = totalPrice.add(totalPrice.multiply(BigDecimal.valueOf(promotion.getPercentage())));
        }

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

    boolean isWithinRange(Date startDate, Date endDate) {
        Date today = Instant.now().toDate();

        return today.after(startDate) && today.before(endDate);
    }
}
