package com.mtech.recycler.controller;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.dto.LoginRequestDto;
import com.mtech.recycler.dto.LoginResponseDto;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.service.Impl.LoginServiceImpl;
import com.mtech.recycler.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private LoginRequestDto loginRequestDto;

    @MockBean
    private LoginServiceImpl loginService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setupEach() {
        loginRequestDto = new LoginRequestDto("username", "password");
    }

    @Test
    void givenLoginRequest_AbleToReachApi() throws Exception {

        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(status().isOk());
    }

    @Test
    void givenLoginRequest_returnSuccessfulResponse() throws Exception {
        String requestJsonString = Utilities.asJsonString(loginRequestDto);
        LoginResponseDto response = new LoginResponseDto("access-token");
        given(loginService.authenticate(loginRequestDto)).willReturn(Optional.of(response));

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("accessToken", is("access-token")))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)))
                .andExpect(jsonPath("message", is(CommonConstant.Message.SUCCESSFUL_REQUEST)));
    }

    @Test
    void givenLoginRequestPropertiesAreEmpty_returnBadRequest() throws Exception {
        loginRequestDto.setEmail("");
        loginRequestDto.setPassword("");
        String requestJsonString = Utilities.asJsonString(loginRequestDto);
        LoginResponseDto response = new LoginResponseDto("access-token");

        given(loginService.authenticate(loginRequestDto)).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, CommonConstant.ErrorMessage.INVALID_REQUEST));

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenLoginRequestUserNotFound_returnBadRequest() throws Exception {
        loginRequestDto.setEmail("");
        loginRequestDto.setPassword("");
        String requestJsonString = Utilities.asJsonString(loginRequestDto);

        given(loginService.authenticate(loginRequestDto)).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user name xxx cannot be found"));

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenLoginRequestCouldNotAuthenticate_returnBadRequest() throws Exception {
        String requestJsonString = Utilities.asJsonString(loginRequestDto);

        given(loginService.authenticate(loginRequestDto)).willReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenLoginRequestNullFromAuthenticate_returnBadRequest() throws Exception {
        String requestJsonString = Utilities.asJsonString(loginRequestDto);

        given(loginService.authenticate(loginRequestDto)).willReturn(Optional.ofNullable(null));

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenLoginRequestThrowsUserNotFoundException_returnNotFound() throws Exception {
        String requestJsonString = Utilities.asJsonString(loginRequestDto);

        given(loginService.authenticate(loginRequestDto)).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found"));

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

