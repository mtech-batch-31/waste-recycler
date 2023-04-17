package com.mtech.recycler.repository;

import com.mtech.recycler.entity.Promotion;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface PromotionRepository extends CrudRepository<Promotion, String> {

    Optional<Promotion> findDiscountByPromotionCode(String promotionCode);
}
