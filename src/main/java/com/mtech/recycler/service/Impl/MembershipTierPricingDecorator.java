package com.mtech.recycler.service.Impl;

import com.mtech.recycler.dto.ItemDto;
import com.mtech.recycler.service.pricingstrategy.PromotionPricingStrategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MembershipTierPricingDecorator extends PricingDecorator {

    private final String membershipTier;
    public MembershipTierPricingDecorator(PromotionPricingStrategy pricingStrategy, String membershipTier) {
        super(pricingStrategy);
        this.membershipTier = membershipTier;
    }

    @Override
    public BigDecimal calculateTotalPrice(List<ItemDto> itemDtos, String promoCode) {
        double tierRate = getTierRate(membershipTier);
        return this.pricingStrategy.calculateTotalPrice(itemDtos, promoCode).multiply(BigDecimal.valueOf(tierRate)).setScale(2, RoundingMode.HALF_UP);
    }


    private double getTierRate(String tier) {
        return switch (tier) {
            case "Bronze" -> 1.5;
            case "Silver" -> 2;
            case "Gold" -> 2.5;
            case "Platinum" -> 3;
            default -> 1;
        };
    }
}
