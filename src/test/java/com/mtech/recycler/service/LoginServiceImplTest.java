package com.mtech.recycler.service;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.dto.LoginRequestDto;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.dto.LoginResponseDto;
import com.mtech.recycler.dto.Role;
import com.mtech.recycler.service.Impl.LoginServiceImpl;
import com.mtech.recycler.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


public class LoginServiceImplTest {

    private String email;
    private String password;

    private LoginRequestDto loginRequestDto;

    private UserService userService;

    private LoginService loginService;

    @BeforeEach
    public void init() {
        userService = Mockito.mock(UserServiceImpl.class);
        JwtTokenProvider tokenProvider = new JwtTokenProvider("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 999999999);
        email = "test@test.com";
        password = "12345";
        loginService = new LoginServiceImpl(tokenProvider, userService);
        loginRequestDto = new LoginRequestDto(email, password);
    }


    @Test
    void givenAuthenticateWhenPasswordDoesNotMatch_ReturnEmpty() {

        var user = new User();
        user.setEmail(email);
        user.setPassword("$2a$10$lH929tpdGIGZK/CwJ3t4muLWrlFuvQDTDiBb4uLQgeRt9bNy4uDgy");


        Mockito.when(userService.getUserByEmail(any(String.class))).thenReturn(user);
        loginRequestDto.setPassword("wrongPassword");
        Optional<LoginResponseDto> loginResponse = loginService.authenticate(loginRequestDto);

        Assertions.assertNotNull(loginResponse);
        Assertions.assertTrue(loginResponse.isPresent());
        Assertions.assertEquals(CommonConstant.ErrorMessage.WRONG_USER_NAME_OR_PASSWORD, loginResponse.get().getMessage());
        Assertions.assertNotNull(CommonConstant.ReturnCode.WRONG_USER_NAME_OR_PASSWORD, loginResponse.get().getReturnCode());
        Mockito.verify(userService, Mockito.times(1)).getUserByEmail(any());
    }

    @Test
    void givenAuthenticate_ReturnAccessToken() {

        var user = new User();
        user.setEmail(email);
        user.setPassword("$2a$10$lH929tpdGIGZK/CwJ3t4muLWrlFuvQDTDiBb4uLQgeRt9bNy4uDgy");
        user.setRole(Role.CUSTOMER);


        Mockito.when(userService.getUserByEmail(any(String.class))).thenReturn(user);

        Optional<LoginResponseDto> loginResponse = loginService.authenticate(loginRequestDto);

        Assertions.assertNotNull(loginResponse);
        Assertions.assertTrue(loginResponse.isPresent());
        Assertions.assertNotNull(loginResponse.get().getAccessToken());
        Mockito.verify(userService, Mockito.times(1)).getUserByEmail(any());
    }

    @Test
    void givenWhenUserNotFound_WillThrowException() {

        var user = new User();
        user.setEmail(email);
        user.setPassword("$2a$10$lH929tpdGIGZK/CwJ3t4muLWrlFuvQDTDiBb4uLQgeRt9bNy4uDgy");

        Mockito.when(userService.getUserByEmail(any(String.class))).thenThrow(ResponseStatusException.class);

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            Optional<LoginResponseDto> loginResponse = loginService.authenticate(loginRequestDto);

            Assertions.assertNull(loginResponse);
        });


        Mockito.verify(userService, Mockito.times(1)).getUserByEmail(any());
    }
}
