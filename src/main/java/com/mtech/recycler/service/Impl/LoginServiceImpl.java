package com.mtech.recycler.service.Impl;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.model.LoginResponse;
import com.mtech.recycler.service.LoginService;
import com.mtech.recycler.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.mtech.recycler.helper.Utilities.isMatchedPassword;

@Slf4j
@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    final private JwtTokenProvider tokenProvider;
    final private UserService userService;

    public LoginServiceImpl(JwtTokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    public Optional<LoginResponse> authenticate(String email, String rawInputPassword) {
        log.info("Login Service Start");
        LoginResponse response = new LoginResponse();
        User user = userService.getUserByEmail(email);

        boolean isMatched = isMatchedPassword(rawInputPassword, user.getPassword());

        log.info("is matched: " + isMatched);

        if (!isMatched) {
            response.setMessage(CommonConstant.ErrorMessage.WRONG_USER_NAME_OR_PASSWORD);
            response.setReturnCode(CommonConstant.ReturnCode.WRONG_USER_NAME_OR_PASSWORD);
            return Optional.of(response);
        }

        response.setAccessToken(tokenProvider.generateToken(email, user.getRole().toString()));

        log.info("Token: " + response.getAccessToken());

        log.info("Login Service end");
        return Optional.of(response);
    }
}
