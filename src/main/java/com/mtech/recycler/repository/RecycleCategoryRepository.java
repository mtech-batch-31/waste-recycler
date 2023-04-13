package com.mtech.recycler.repository;

import com.mtech.recycler.entity.RecycleCategory;
import org.springframework.data.repository.CrudRepository;

public interface RecycleCategoryRepository extends CrudRepository<RecycleCategory, String> {
}
