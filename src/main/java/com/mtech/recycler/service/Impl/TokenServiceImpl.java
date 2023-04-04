package com.mtech.recycler.service.Impl;

import com.mtech.recycler.common.CommonConstant;
import com.mtech.recycler.entity.RefreshToken;
import com.mtech.recycler.exception.RefreshTokenExpiredException;
import com.mtech.recycler.repository.RefreshTokenRepository;
import com.mtech.recycler.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class TokenServiceImpl implements TokenService {

    final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public TokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken getTokenByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RefreshTokenExpiredException(CommonConstant.ErrorMessage.REFRESH_TOKEN_EXPIRED));
    }

    @Override
    public RefreshToken verifyTokenExpiration(RefreshToken token) {
        if (token.getTokenExpiryDateTime().compareTo(Instant.now().toDate()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenExpiredException(CommonConstant.ErrorMessage.REFRESH_TOKEN_EXPIRED);
        }

        return token;
    }

    @Override
    public void addOrUpdateRefreshToken(RefreshToken token) {
        refreshTokenRepository.save(token);
    }
}
