package com.mtech.recycler.service.Impl;

import com.mtech.recycler.dto.ItemDto;
import com.mtech.recycler.service.pricingstrategy.PromotionPricingStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class LoyaltyPointPricingDecorator extends PricingDecorator {

    private final double loyaltyPoint;
    public LoyaltyPointPricingDecorator(PromotionPricingStrategy pricingStrategy, double loyaltyPoint) {
        super(pricingStrategy);
        this.loyaltyPoint = loyaltyPoint;
    }

    @Override
    public BigDecimal calculateTotalPrice(List<ItemDto> itemDtos, String promoCode) {
        double dollars =  convertPointToBalance(loyaltyPoint);
        return this.pricingStrategy.calculateTotalPrice(itemDtos, promoCode).add(BigDecimal.valueOf(dollars)).setScale(2, RoundingMode.HALF_UP);
    }


    private double convertPointToBalance(double memberPoint) {
        double cents = memberPoint / 10;
        return cents / 100;
    }
}
