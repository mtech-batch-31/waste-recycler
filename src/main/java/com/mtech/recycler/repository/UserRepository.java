package com.mtech.recycler.repository;

import com.mtech.recycler.model.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findById(String id);
}
