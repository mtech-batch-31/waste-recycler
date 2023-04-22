package com.mtech.recycler.repository;

import com.mtech.recycler.entity.RecycleCategory;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface RecycleCategoryRepository extends CrudRepository<RecycleCategory, String> {

    Optional<RecycleCategory> findByName(String name);
}
