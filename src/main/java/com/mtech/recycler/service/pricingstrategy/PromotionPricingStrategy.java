package com.mtech.recycler.service.pricingstrategy;

import com.mtech.recycler.model.Category;
import com.mtech.recycler.model.Item;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionPricingStrategy {
        BigDecimal calculateTotalPrice(List<Category> categories, String promoCode, List<Item> items);
        List<Item> calculateSubTotalPrice(List<Category> categories, String promoCode, List<Item> items);

}
