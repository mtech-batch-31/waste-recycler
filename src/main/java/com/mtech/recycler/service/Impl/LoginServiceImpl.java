package com.mtech.recycler.service.Impl;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.model.LoginResponse;
import com.mtech.recycler.service.LoginService;
import com.mtech.recycler.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    final private JwtTokenProvider tokenProvider;

    final private UserService userService;

    @Autowired
    public LoginServiceImpl(JwtTokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    public Optional<LoginResponse> authenticate(String userName, String password) {
        log.info("Login Service Start");
        LoginResponse response = new LoginResponse();

        User user = userService.getUserByUserName(userName);
        response.setAccessToken(tokenProvider.generateToken(userName));

        log.info("Login Service end");
        return Optional.of(response);
    }
}
