package com.mtech.recycler.repository;

import com.mtech.recycler.entity.RecycleItem;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface RecycleItemRepository extends CrudRepository<RecycleItem, String> {

    List<RecycleItem> findByEmail(String email);
}