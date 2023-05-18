package com.mtech.recycler.service.pricingstrategy;

import com.mtech.recycler.dto.CategoryDto;
import com.mtech.recycler.dto.ItemDto;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionPricingStrategy {
        BigDecimal calculateTotalPrice(List<CategoryDto> categories, String promoCode, List<ItemDto> itemDtos);
        List<ItemDto> calculateSubTotalPrice(List<CategoryDto> categories, String promoCode, List<ItemDto> itemDtos);

}
