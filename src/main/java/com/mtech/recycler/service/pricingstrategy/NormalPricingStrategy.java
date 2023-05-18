package com.mtech.recycler.service.pricingstrategy;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.Promotion;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.dto.CategoryDto;
import com.mtech.recycler.dto.ItemDto;
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
public class NormalPricingStrategy implements PromotionPricingStrategy {

    private final RecycleCategoryRepository recycleCategoryRepository;
    private final PromotionRepository promotionRepository;

    public NormalPricingStrategy(RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository) {
        this.recycleCategoryRepository = recycleCategoryRepository;
        this.promotionRepository = promotionRepository;
    }

    private boolean isWithinRange(Date startDate, Date endDate) {
        Date currentDate = new Date();
        return currentDate.after(startDate) && currentDate.before(endDate);
    }
    private static final Logger log = LoggerFactory.getLogger(NormalPricingStrategy.class);

    @Override
    public BigDecimal calculateTotalPrice(List<CategoryDto> categories, String promoCode, List<ItemDto> itemDtos) {
     BigDecimal totalPrice = categories.stream().map(c -> {
        BigDecimal unitPrice = recycleCategoryRepository.findByName(c.getCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The following category name (%s) is not found".formatted(c.getCategory())))
                .getPrice();
        BigDecimal subTotalPrice = unitPrice.multiply(BigDecimal.valueOf(c.getQuantity()));
        itemDtos.add(new ItemDto(c.getCategory(), c.getQuantity(), unitPrice, subTotalPrice, ""));
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
        totalPrice = totalPrice.multiply(BigDecimal.valueOf(1));
    }
    return totalPrice;
    }

    public List<ItemDto> calculateSubTotalPrice(List<CategoryDto> categories, String promoCode, List<ItemDto> itemDtos) {

        if (StringUtils.hasText(promoCode)) {
            Promotion promotion = promotionRepository.findDiscountByPromotionCode(promoCode)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonConstant.ErrorMessage.INVALID_PROMOTION_CODE));

            if (!isWithinRange(promotion.getStartDate(), promotion.getEndDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonConstant.ErrorMessage.EXPIRED_PROMOTION_CODE);
            }

            Utilities.updateSubTotalPriceWithPromotion(itemDtos, promotion.getPercentage(), BigDecimal.valueOf(1));
        }
        return itemDtos;
    }
}


