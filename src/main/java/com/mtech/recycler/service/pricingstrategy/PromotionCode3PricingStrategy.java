package com.mtech.recycler.service.pricingstrategy;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.Category;
import com.mtech.recycler.model.Item;
import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class PromotionCode3PricingStrategy implements PromotionPricingStrategy {

    private final RecycleCategoryRepository recycleCategoryRepository;
    private final PromotionRepository promotionRepository;


    public PromotionCode3PricingStrategy(RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository) {
        this.recycleCategoryRepository = recycleCategoryRepository;
        this.promotionRepository = promotionRepository;
    }

    private boolean isWithinRange(Date startDate, Date endDate) {
        Date currentDate = new Date();
        return currentDate.after(startDate) && currentDate.before(endDate);
    }
    private static final Logger log = LoggerFactory.getLogger(PromotionCode1PricingStrategy.class);

    @Override
    public BigDecimal calculateTotalPrice(List<Category> categories, String promoCode, List<Item> items) {
        BigDecimal totalPrice = categories.stream().map(c -> {
            BigDecimal unitPrice = recycleCategoryRepository.findByName(c.getCategory())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The following category name (%s) is not found".formatted(c.getCategory())))
                    .getPrice();
            BigDecimal subTotalPrice = unitPrice.multiply(BigDecimal.valueOf(c.getQuantity()));
            items.add(new Item(c.getCategory(), c.getQuantity(), unitPrice, subTotalPrice, ""));
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
            totalPrice = totalPrice.multiply(BigDecimal.valueOf(1.6));
        }
        return totalPrice;
    }

    public List<Item> calculateSubTotalPrice(List<Category> categories, String promoCode, List<Item> items) {

        if (StringUtils.hasText(promoCode)) {
            Promotion promotion = promotionRepository.findDiscountByPromotionCode(promoCode)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonConstant.ErrorMessage.INVALID_PROMOTION_CODE));

            if (!isWithinRange(promotion.getStartDate(), promotion.getEndDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonConstant.ErrorMessage.EXPIRED_PROMOTION_CODE);
            }

            Utilities.updateSubTotalPriceWithPromotion(items, promotion.getPercentage(), BigDecimal.valueOf(1.6));
        }
        return items;
    }
}


