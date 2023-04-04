package com.mtech.recycler.service;

import com.mtech.recycler.entity.RefreshToken;

public interface TokenService {

    RefreshToken getTokenByRefreshToken(String refreshToken);

    RefreshToken verifyTokenExpiration(RefreshToken token);

    void addOrUpdateRefreshToken(RefreshToken token);
}
