package com.mtech.recycler.service.Impl;

import com.mtech.recycler.dto.ItemDto;
import com.mtech.recycler.service.pricingstrategy.PromotionPricingStrategy;

import java.math.BigDecimal;
import java.util.List;

public class PricingDecorator implements PromotionPricingStrategy {

    protected PromotionPricingStrategy pricingStrategy;

    public PricingDecorator(PromotionPricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    @Override
    public BigDecimal calculateTotalPrice(List<ItemDto> itemDtos, String promoCode) {
        return this.pricingStrategy.calculateTotalPrice(itemDtos, promoCode);
    }

}
