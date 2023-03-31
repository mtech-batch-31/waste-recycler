package com.mtech.recycler.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.model.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import(SecurityConfig.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private LoginRequest request;

    @BeforeEach
    public void setup() {
        request = new LoginRequest("username", "password");
    }

    @Test
    public void testRun_Successful() throws Exception {
        mockMvc.perform(get("/api/v1/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("token", is("testing")));
    }

    @Test
    public void shouldReturnTokenAfterLoggedIn_Successful() throws Exception {
        String requestJsonString = asJsonString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("token", is("testing")));
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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
