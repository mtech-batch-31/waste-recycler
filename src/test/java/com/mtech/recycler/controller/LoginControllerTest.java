package com.mtech.recycler.controller;

import com.mtech.recycler.common.CommonConstant;
import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.exception.UserNotFoundException;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.LoginRequest;
import com.mtech.recycler.model.LoginResponse;
import com.mtech.recycler.model.RefreshTokenRequest;
import com.mtech.recycler.service.Impl.LoginServiceImpl;
import com.mtech.recycler.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private LoginRequest loginRequest;
    private RefreshTokenRequest tokenRequest;

    @MockBean
    private LoginServiceImpl loginService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setupEach() {
        loginRequest = new LoginRequest("username", "password");
        tokenRequest = new RefreshTokenRequest("refreshToken");
    }

    @Test
    public void givenLoginRequest_AbleToReachApi() throws Exception {

        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(status().isOk());
    }

    @Test
    public void givenLoginRequest_returnSuccessfulResponse() throws Exception {
        String requestJsonString = Utilities.asJsonString(loginRequest);
        LoginResponse response = new LoginResponse("access-token", "refresh-token");

        given(loginService.authenticate(any(String.class), any(String.class))).willReturn(Optional.of(response));

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("accessToken", is("access-token")))
                .andExpect(jsonPath("refreshToken", is("refresh-token")))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)))
                .andExpect(jsonPath("message", is(CommonConstant.Message.SUCCESSFUL_REQUEST)));
    }

    @Test
    public void givenLoginRequestPropertiesAreEmpty_returnBadRequest() throws Exception {
        loginRequest.setEmail("");
        loginRequest.setPassword("");
        String requestJsonString = Utilities.asJsonString(loginRequest);
        LoginResponse response = new LoginResponse("access-token", "refresh-token");

        given(loginService.authenticate(any(String.class), any(String.class))).willReturn(Optional.of(response));

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLoginRequestUserNotFound_returnBadRequest() throws Exception {
        loginRequest.setEmail("");
        loginRequest.setPassword("");
        String requestJsonString = Utilities.asJsonString(loginRequest);

        given(loginService.authenticate(any(String.class), any(String.class))).willThrow(UserNotFoundException.class);

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenLoginRequestCouldNotAuthenticate_returnBadRequest() throws Exception {
        String requestJsonString = Utilities.asJsonString(loginRequest);

        given(loginService.authenticate(any(String.class), any(String.class))).willReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenLoginRequestNullFromAuthenticate_returnBadRequest() throws Exception {
        String requestJsonString = Utilities.asJsonString(loginRequest);

        given(loginService.authenticate(any(String.class), any(String.class))).willReturn(Optional.ofNullable(null));

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenRefreshTokenRequest_returnSuccessfulResponse() throws Exception {
        String requestJsonString = Utilities.asJsonString(tokenRequest);
        LoginResponse response = new LoginResponse("access-token", "refresh-token");

        given(loginService.refreshAccessToken(any(String.class))).willReturn(Optional.of(response));

        mockMvc.perform(post("/api/v1/auth/refresh-token").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("accessToken", is("access-token")))
                .andExpect(jsonPath("refreshToken", is("refresh-token")))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)))
                .andExpect(jsonPath("message", is(CommonConstant.Message.SUCCESSFUL_REQUEST)));
    }

    @Test
    public void givenRefreshTokenRequestPropertiesAreEmpty_returnBadRequest() throws Exception {
        tokenRequest.setRefreshToken("");
        String requestJsonString = Utilities.asJsonString(tokenRequest);
        LoginResponse response = new LoginResponse("access-token", "refresh-token");

        given(loginService.refreshAccessToken(any(String.class))).willReturn(Optional.of(response));

        mockMvc.perform(post("/api/v1/auth/refresh-token").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenRefreshTokenRequestRefreshAccessReturnsEmpty_returnBadRequest() throws Exception {
        String requestJsonString = Utilities.asJsonString(tokenRequest);

        given(loginService.refreshAccessToken(any(String.class))).willReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/refresh-token").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
