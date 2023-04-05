package com.mtech.recycler.service;

import com.mtech.recycler.entity.RefreshToken;

import java.util.Optional;

public interface TokenService {

    RefreshToken getTokenByRefreshToken(String refreshToken);

    Optional<RefreshToken> getTokenByEmail(String email);

    RefreshToken verifyTokenExpiration(RefreshToken token);

    void addOrUpdateRefreshToken(RefreshToken token);
}
