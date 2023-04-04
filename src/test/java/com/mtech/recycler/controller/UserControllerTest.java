package com.mtech.recycler.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserService userService;

    @Test
    void givenRegisterRequest_returnSuccessResponse() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/user/register")
                                .content(asJsonString(new RegisterRequest()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("returnCode", Matchers.is("success")));

    }

    @Test
    void callSecuredApi_return401() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/user/secured")
                                .content(asJsonString(new RegisterRequest()))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
