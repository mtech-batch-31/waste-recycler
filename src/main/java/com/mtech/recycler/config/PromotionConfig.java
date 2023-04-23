package com.mtech.recycler.config;

import com.mtech.recycler.repository.PromotionRepository;
import com.mtech.recycler.repository.RecycleCategoryRepository;
import com.mtech.recycler.repository.RecycleItemRepository;
import com.mtech.recycler.service.pricingstrategy.PromotionCode1PricingStrategy;
import com.mtech.recycler.service.pricingstrategy.PromotionCode2PricingStrategy;
import com.mtech.recycler.service.pricingstrategy.PromotionCode3PricingStrategy;
import com.mtech.recycler.service.pricingstrategy.PromotionPricingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PromotionConfig {
    private final RecycleCategoryRepository recycleCategoryRepository;
    private final PromotionRepository promotionRepository;
    private final RecycleItemRepository recycleItemRepository;
    public PromotionConfig(RecycleCategoryRepository recycleCategoryRepository, PromotionRepository promotionRepository, RecycleItemRepository recycleItemRepository) {
        this.recycleCategoryRepository = recycleCategoryRepository;
        this.promotionRepository = promotionRepository;
        this.recycleItemRepository = recycleItemRepository;
    }
    @Bean
    @Primary
    public PromotionPricingStrategy promotionCode1PricingStrategy() {
        return new PromotionCode1PricingStrategy(recycleCategoryRepository, promotionRepository, recycleItemRepository);
    }
    @Bean
    public PromotionCode2PricingStrategy promotionCode2PricingStrategy() {
        return new PromotionCode2PricingStrategy(recycleCategoryRepository, promotionRepository, recycleItemRepository);
    }
    @Bean
    public PromotionCode3PricingStrategy promotionCode3PricingStrategy() {
        return new PromotionCode3PricingStrategy(recycleCategoryRepository, promotionRepository, recycleItemRepository);
    }
}