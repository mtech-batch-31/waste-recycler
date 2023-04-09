package com.mtech.recycler.service;

import com.mtech.recycler.common.CommonConstant;
import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.model.LoginResponse;
import com.mtech.recycler.service.Impl.LoginServiceImpl;
import com.mtech.recycler.service.Impl.UserServiceImpl;
import org.joda.time.Instant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@TestPropertySource(properties = {
        "app.jwtSecret=aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        "app.jwtExpirationInMinutes=999999999"
})
public class LoginServiceImplTest {

    private String email;
    private String password;
    private Date tokenExpiry;
    private String token;

    private UserService userService;

    private LoginService loginService;

    @BeforeEach
    public void init() {
        userService = Mockito.mock(UserServiceImpl.class);
        JwtTokenProvider tokenProvider = new JwtTokenProvider("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 999999999);
        email = "test@test.com";
        password = "12345";
        tokenExpiry = Instant.parse("2023-04-07").toDate();
        token = "token";
        loginService = new LoginServiceImpl(tokenProvider, userService);
    }


    @Test
    void givenAuthenticateWhenPasswordDoesNotMatch_ReturnEmpty() {

        var user = new User();
        user.setEmail(email);
        user.setPassword("$2a$10$lH929tpdGIGZK/CwJ3t4muLWrlFuvQDTDiBb4uLQgeRt9bNy4uDgy");


        Mockito.when(userService.getUserByEmail(any(String.class))).thenReturn(user);

        Optional<LoginResponse> loginResponse = loginService.authenticate("test@test.com", "456");

        Assertions.assertNotNull(loginResponse);
        Assertions.assertTrue(loginResponse.isPresent());
        Assertions.assertEquals(CommonConstant.ErrorMessage.WRONG_USER_NAME_OR_PASSWORD, loginResponse.get().getMessage());
        Assertions.assertNotNull(CommonConstant.ReturnCode.WRONG_USER_NAME_OR_PASSWORD, loginResponse.get().getReturnCode());
        Mockito.verify(userService, Mockito.times(1)).getUserByEmail(any());
    }
}