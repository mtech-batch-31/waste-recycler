package com.mtech.recycler.controller;


import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.dto.RegisterRequestDto;
import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserService userService;

    private Customer customer;


    @BeforeEach
    public void setupEach() {
        customer = new Customer();
    }

    @Test
    void givenRegisterRequest_returnSuccessResponse() throws Exception {

        given(userService.createCustomer(any(RegisterRequestDto.class))).willReturn(customer);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/user/register")
                                .content(Utilities.asJsonString(new RegisterRequestDto()))
                                .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("returnCode", Matchers.is("00")));
    }

    @Test
    void givenRegisterRequest_returnErrorResponse() throws Exception {

        given(userService.createCustomer(any(RegisterRequestDto.class))).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists"));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/user/register")
                                .content(Utilities.asJsonString(new RegisterRequestDto()))
                                .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("returnCode", Matchers.is(String.valueOf(HttpStatus.CONFLICT.value()))));
    }

    @Test
    void callSecuredApi_return401() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/user/secured")
                                .content(Utilities.asJsonString(new RegisterRequestDto()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    @Test
    public void callSecuredApi_return200() throws Exception {
        given(jwtTokenProvider.validateToken(any(String.class))).willReturn(true);
        given(jwtTokenProvider.getUserNameFromJWT(any(String.class))).willReturn("user");
        given(userService.getUserByEmail(any(String.class))).willReturn(new User());
        given(jwtTokenProvider.getRoleFromJWT(any(String.class))).willReturn("test");

        mockMvc.perform(get("/api/v1/user/secured").header("Authorization", "Bearer 12334"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)));

    }

    @Test
    public void callRegistrationConfirm_return200() throws Exception {
        given(jwtTokenProvider.validateToken(any(String.class))).willReturn(true);
        given(jwtTokenProvider.getUserNameFromJWT(any(String.class))).willReturn("user");
        given(jwtTokenProvider.getRoleFromJWT(any(String.class))).willReturn("test");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/user/registrationConfirm?token=test")
                        .contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)));

    }
}
