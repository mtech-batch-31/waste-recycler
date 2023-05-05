package com.mtech.recycler.repository;

import com.mtech.recycler.entity.RecycleRequest;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface RecycleRequestRepository extends CrudRepository<RecycleRequest, String> {

    List<RecycleRequest> findByEmail(String email);
}
