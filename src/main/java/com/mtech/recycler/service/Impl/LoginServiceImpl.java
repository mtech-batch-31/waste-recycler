package com.mtech.recycler.service.Impl;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.dto.LoginRequestDto;
import com.mtech.recycler.dto.LoginResponseDto;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.helper.Logger;
import com.mtech.recycler.service.LoginService;
import com.mtech.recycler.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.mtech.recycler.helper.Utilities.isMatchedPassword;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

    private final Logger log = Logger.getInstance();
    final private JwtTokenProvider tokenProvider;
    final private UserService userService;

    public LoginServiceImpl(JwtTokenProvider tokenProvider, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    public Optional<LoginResponseDto> authenticate(LoginRequestDto loginRequestDto) {
        log.info("Login Service Start");

        if (!StringUtils.hasText(loginRequestDto.getEmail()) || !StringUtils.hasText(loginRequestDto.getPassword())) {
//            return ResponseEntity.badRequest().body(CommonConstant.ErrorMessage.INVALID_REQUEST);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonConstant.ErrorMessage.INVALID_REQUEST);
        }
        String email = loginRequestDto.getEmail();
        LoginResponseDto response = new LoginResponseDto();
        User user = userService.getUserByEmail(email);

        boolean isMatched = isMatchedPassword(loginRequestDto.getPassword(), user.getPassword());

        log.info("is matched: " + isMatched);

        if (!isMatched) {
            response.setMessage(CommonConstant.ErrorMessage.WRONG_USER_NAME_OR_PASSWORD);
            response.setReturnCode(CommonConstant.ReturnCode.WRONG_USER_NAME_OR_PASSWORD);
            return Optional.of(response);
        }

        response.setAccessToken(tokenProvider.generateToken(loginRequestDto.getEmail(), user.getRole().toString()));

        log.info("Token: " + response.getAccessToken());

        log.info("Login Service end");
        return Optional.of(response);
    }
}
