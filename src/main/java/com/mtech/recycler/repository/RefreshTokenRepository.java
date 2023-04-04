package com.mtech.recycler.repository;

import com.mtech.recycler.entity.RefreshToken;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
