package com.mtech.recycler.service.Impl;

import com.mtech.recycler.common.CommonConstant;
import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.entity.RefreshToken;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.model.LoginResponse;
import com.mtech.recycler.service.LoginService;
import com.mtech.recycler.service.TokenService;
import com.mtech.recycler.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.mtech.recycler.helper.Utilities.isMatchedPassword;

@Slf4j
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    @Value("${app.refreshTokenExpirationInMinutes}")
    private int refreshTokenExpirationInMinutes;

    final private JwtTokenProvider tokenProvider;

    final private UserService userService;

    final private TokenService tokenService;

    @Autowired
    public LoginServiceImpl(JwtTokenProvider tokenProvider, UserService userService, TokenService tokenService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<LoginResponse> authenticate(String email, String rawInputPassword) {
        log.info("Login Service Start");
        LoginResponse response = new LoginResponse();
        String refreshToken = UUID.randomUUID().toString();

        User user = userService.getUserByEmail(email);

        boolean isMatched = isMatchedPassword(rawInputPassword, user.getPassword());

        if (!isMatched) {
            response.setMessage(CommonConstant.ErrorMessage.WRONG_USER_NAME_OR_PASSWORD);
            response.setReturnCode(CommonConstant.ReturnCode.WRONG_USER_NAME_OR_PASSWORD);
            return Optional.of(response);
        }

        Optional<RefreshToken> existingToken = tokenService.getTokenByEmail(email);

        if (existingToken.isPresent())
            refreshToken = existingToken.get().getRefreshToken();

        addOrUpdateToken(user.getEmail(), refreshToken);
        response.setAccessToken(tokenProvider.generateToken(email));
        response.setRefreshToken(refreshToken);

        log.info("Login Service end");
        return Optional.of(response);
    }

    @Override
    public Optional<LoginResponse> refreshAccessToken(String refreshToken) {
        log.info("Refresh Access Token Service Start");
        var response = new LoginResponse();

        RefreshToken token = tokenService.getTokenByRefreshToken(refreshToken);

        token = tokenService.verifyTokenExpiration(token);
        addOrUpdateToken(token.getId(), token.getRefreshToken());

        response.setRefreshToken(refreshToken);
        response.setAccessToken(tokenProvider.generateToken(token.getId()));

        log.info("Refresh Access Token Service End");

        return Optional.of(response);
    }

    private void addOrUpdateToken(String id, String refreshToken) {
        var token = new RefreshToken();
        token.setId(id);
        token.setRefreshToken(refreshToken);

        var currentExpiration = new DateTime(Instant.now().toDate());
        Date newExpiration = currentExpiration.plusMinutes(refreshTokenExpirationInMinutes).toDate();
        token.setTokenExpiryDateTime(newExpiration);
        tokenService.addOrUpdateRefreshToken(token);
    }


}
