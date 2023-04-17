package com.mtech.recycler.repository;

import com.mtech.recycler.entity.RecycleCategory;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface RecycleCategoryRepository extends CrudRepository<RecycleCategory, String> {

    RecycleCategory findByName(String name);
}
