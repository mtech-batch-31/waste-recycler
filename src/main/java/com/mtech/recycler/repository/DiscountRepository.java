package com.mtech.recycler.repository;

import com.mtech.recycler.entity.Discount;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface DiscountRepository extends CrudRepository<Discount, String> {

    Optional<Discount> findDiscountByDiscountCode(String discountCode);
}
