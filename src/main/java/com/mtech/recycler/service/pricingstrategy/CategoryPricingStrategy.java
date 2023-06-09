package com.mtech.recycler.service.pricingstrategy;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.Promotion;
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
public class CategoryPricingStrategy implements PromotionPricingStrategy {

    private final RecycleCategoryRepository recycleCategoryRepository;
    private final PromotionRepository promotionRepository;


    public CategoryPricingStrategy(RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository) {
        this.recycleCategoryRepository = recycleCategoryRepository;
        this.promotionRepository = promotionRepository;
    }

    private boolean isWithinRange(Date startDate, Date endDate) {
        Date currentDate = new Date();
        return currentDate.after(startDate) && currentDate.before(endDate);
    }
    private static final Logger log = LoggerFactory.getLogger(CategoryPricingStrategy.class);

    @Override
    public BigDecimal calculateTotalPrice(List<ItemDto> itemDtos, String promoCode) {
        BigDecimal totalPrice = null;
        if (StringUtils.hasText(promoCode)) {
            Promotion promotion = promotionRepository.findDiscountByPromotionCode(promoCode)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CommonConstant.ErrorMessage.INVALID_PROMOTION_CODE));

            if (!isWithinRange(promotion.getStartDate(), promotion.getEndDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonConstant.ErrorMessage.EXPIRED_PROMOTION_CODE);
            }

            totalPrice = itemDtos.stream().map(c -> {
            BigDecimal unitPrice = recycleCategoryRepository.findByName(c.getCategory())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The following category name (%s) is not found".formatted(c.getCategory())))
                    .getPrice();
            BigDecimal subTotalPrice;
            subTotalPrice = unitPrice.multiply(BigDecimal.valueOf(c.getQuantity()));

            if (c.getCategory().equalsIgnoreCase(promotion.getDescription())) {
                subTotalPrice = subTotalPrice.add(subTotalPrice.multiply(BigDecimal.valueOf(promotion.getPercentage())));
            }
            c.setSubTotalPrice(subTotalPrice.setScale(2, RoundingMode.CEILING));
            return subTotalPrice;
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
        log.info("CategoryPricingStrategy - calculateTotalPrice - total Price is :%s".formatted(totalPrice));
        return totalPrice;
    }

}


