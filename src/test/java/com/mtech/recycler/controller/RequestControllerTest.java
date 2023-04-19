package com.mtech.recycler.controller;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.Category;
import com.mtech.recycler.model.PricingRequest;
import com.mtech.recycler.model.PricingResponse;
import com.mtech.recycler.service.Impl.RequestServiceImpl;
import com.mtech.recycler.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@Import(SecurityConfig.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private PricingRequest request;

    @MockBean
    private RequestServiceImpl requestService;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private UserServiceImpl userService;


    @BeforeEach
    public void setupEach() {
        List<Category> categories = new ArrayList<>() {{
            add(new Category("Electronics", new BigDecimal(700), 0, "item"));
            add(new Category("Battery", new BigDecimal(500), 0, "kg"));
        }};

        request = new PricingRequest("promo", categories);

    }

    private void initAuth() {
        given(tokenProvider.validateToken(any(String.class))).willReturn(true);
        given(tokenProvider.getUserNameFromJWT(any(String.class))).willReturn("user");
        given(userService.getUserByEmail(any(String.class))).willReturn(new User());
        given(tokenProvider.getRoleFromJWT(any(String.class))).willReturn("test");
    }

    @Test
    public void givenRecyclingRequestWhenSendForPricing_returnSuccessfulResponse() throws Exception {
        initAuth();
        String expectedResponse = "{\"returnCode\":\"00\",\"message\":\"The request has been successfully processed\",\"totalPrice\":100,\"items\":[{\"name\":\"test\",\"quantity\":1,\"price\":50,\"totalPrice\":50},{\"name\":\"test2\",\"quantity\":1,\"price\":50,\"totalPrice\":50}]}";
        String requestJsonString = Utilities.asJsonString(request);
        List<PricingResponse.Items> items = new ArrayList<>() {{
            add(new PricingResponse.Items("test", 1, new BigDecimal(50), new BigDecimal(50)));
            add(new PricingResponse.Items("test2", 1, new BigDecimal(50), new BigDecimal(50)));
        }};
        PricingResponse response = new PricingResponse(new BigDecimal(100), items);

        given(requestService.GetRequestTotalPricing(any(PricingRequest.class))).willReturn(Optional.of(response));

        MvcResult result = mockMvc.perform(post("/api/v1/request/categories").content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)))
                .andExpect(jsonPath("message", is(CommonConstant.Message.SUCCESSFUL_REQUEST)))
                .andReturn();

        Assertions.assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }

//    @Test
//    public void givenLoginRequestPropertiesAreEmpty_returnBadRequest() throws Exception {
//        loginRequest.setEmail("");
//        loginRequest.setPassword("");
//        String requestJsonString = Utilities.asJsonString(loginRequest);
//        LoginResponse response = new LoginResponse("access-token");
//
//        given(loginService.authenticate(any(String.class), any(String.class))).willReturn(Optional.of(response));
//
//        mockMvc.perform(post("/api/v1/auth/login").content(requestJsonString).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
}
