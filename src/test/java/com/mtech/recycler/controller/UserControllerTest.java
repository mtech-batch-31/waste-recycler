package com.mtech.recycler.controller;


import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.RegisterRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

        given(userService.createCustomer(any(RegisterRequest.class))).willReturn(customer);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/user/register")
                                .content(Utilities.asJsonString(new RegisterRequest()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("returnCode", Matchers.is("00")));
    }

    @Test
    void givenRegisterRequest_returnErrorResponse() throws Exception {

        given(userService.createCustomer(any(RegisterRequest.class))).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists"));

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/user/register")
                                .content(Utilities.asJsonString(new RegisterRequest()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("returnCode", Matchers.is(String.valueOf(HttpStatus.CONFLICT.value()))));
    }

    @Test
    void callSecuredApi_return401() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/user/secured")
                                .content(Utilities.asJsonString(new RegisterRequest()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }
}
