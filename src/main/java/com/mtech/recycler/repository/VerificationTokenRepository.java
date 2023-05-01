package com.mtech.recycler.repository;

import com.mtech.recycler.entity.User;
import com.mtech.recycler.entity.VerificationToken;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, String> {
    Optional<VerificationToken> findById(String id);

    Optional<VerificationToken> findByToken(String token);
}
