package com.mtech.recycler.service.Impl;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.model.LoginResponse;
import com.mtech.recycler.service.LoginService;
import com.mtech.recycler.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    final JwtTokenProvider tokenProvider;

    final UserService userService;

    @Autowired
    public LoginServiceImpl(JwtTokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    public Optional<LoginResponse> authenticate(String userName, String password) {
        LoginResponse response = new LoginResponse();

        response.setToken(tokenProvider.generateToken(userName));

        return Optional.of(response);
    }
}
