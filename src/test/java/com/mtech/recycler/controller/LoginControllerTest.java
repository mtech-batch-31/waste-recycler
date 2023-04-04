package com.mtech.recycler.controller;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.LoginRequest;
import com.mtech.recycler.model.LoginResponse;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private LoginRequest request;

    @MockBean
    private LoginServiceImpl loginService;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setup() {
        request = new LoginRequest("username", "password");
    }

    @Test
    public void givenLoginRequest_AbleToReachApi() throws Exception {

        mockMvc.perform(get("/api/v1/auth/test"))
                .andExpect(status().isOk());
    }

    @Test
    public void givenLoginRequest_returnSuccessfulResponse() throws Exception {
        String requestJsonString = Utilities.asJsonString(request);
        LoginResponse response = new LoginResponse("access-token", "refresh-token");

        given(loginService.authenticate(request.getUserName(), request.getPassword())).willReturn(Optional.of(response));

        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("accessToken", is("access-token")));
    }

//    @Test
//    void shouldReturnTokenAfterLoggedIn() throws Exception {
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/api/v1/login")
//                                .content(asJsonString(request))
//                                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("status", Matchers.is("success")));
//    }


}
