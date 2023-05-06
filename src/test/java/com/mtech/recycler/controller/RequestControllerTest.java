package com.mtech.recycler.controller;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.Category;
import com.mtech.recycler.model.Item;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@WebMvcTest(RequestController.class)
@Import(SecurityConfig.class)
public class RequestControllerTest {

    private final String ENDPOINT_CALCULATE_PRICING = "/api/v1/request/price";

    private final String ENDPOINT_GET_CATEGORIES = "/api/v1/request/categories";

    private final String ENDPOINT_RETRIEVE_REQUEST = "/api/v1/request/retrieve";

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
            add(new Category("Electronics", new BigDecimal(700), 0.1, "item", ""));
            add(new Category("Battery", new BigDecimal(500), 1, "kg", ""));
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
        String expectedResponse = "{\"returnCode\":\"00\",\"message\":\"The request has been successfully processed\",\"totalPrice\":100,\"items\":[{\"category\":\"test\",\"quantity\":1.0,\"unitPrice\":50,\"subTotalPrice\":50,\"description\":\"\"},{\"category\":\"test2\",\"quantity\":1.0,\"unitPrice\":50,\"subTotalPrice\":50,\"description\":\"\"}]}";
        String requestJsonString = Utilities.asJsonString(request);
        List<Item> items = new ArrayList<>() {{
            add(new Item("test", 1, new BigDecimal(50), new BigDecimal(50), ""));
            add(new Item("test2", 1, new BigDecimal(50), new BigDecimal(50), ""));
        }};
        PricingResponse response = new PricingResponse(new BigDecimal(100), items);

        given(requestService.getRequestTotalPricing(any(PricingRequest.class))).willReturn(Optional.of(response));

        MvcResult result = mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)))
                .andExpect(jsonPath("message", is(CommonConstant.Message.SUCCESSFUL_REQUEST)))
                .andReturn();

        Assertions.assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }

    @Test
    public void givenRecyclingRequestWhenSendWithoutCategory_return404Response() throws Exception {
        initAuth();
        request.getData().get(0).setCategory("");
        String requestJsonString = Utilities.asJsonString(request);

        mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("returnCode", is(String.valueOf(HttpStatus.BAD_REQUEST.value()))))
                .andExpect(jsonPath("message", is(CommonConstant.ErrorMessage.CATEGORY_VALIDATION_FAILED)));

    }

    @Test
    public void givenRecyclingRequestWhenQuantityIsLessOrEqualZero_return404Response() throws Exception {
        initAuth();
        request.getData().get(0).setQuantity(0);
        String requestJsonString = Utilities.asJsonString(request);

        mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("returnCode", is(String.valueOf(HttpStatus.BAD_REQUEST.value()))))
                .andExpect(jsonPath("message", is(CommonConstant.ErrorMessage.QUANTITY_VALIDATION_FAILED)));

    }

    @Test
    public void givenRecyclingRequestWhenEmptyObjectFromService_return404Response() throws Exception {
        initAuth();
        String requestJsonString = Utilities.asJsonString(request);

        given(requestService.getRequestTotalPricing(any(PricingRequest.class))).willReturn(Optional.empty());

        mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenRecyclingRequestWhenThrowException_return404Response() throws Exception {
        initAuth();
        String requestJsonString = Utilities.asJsonString(request);

        given(requestService.getRequestTotalPricing(any(PricingRequest.class))).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "error"));

        mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(String.valueOf(HttpStatus.BAD_REQUEST.value()))))
                .andExpect(jsonPath("message", is("error")));
    }

    @Test
    public void givenRecyclingCategoriesRequest_returnSuccessfulResponse() throws Exception {
        initAuth();
        String expectedResponse = "{\"returnCode\":\"00\",\"message\":\"The request has been successfully processed\",\"categories\":[{\"category\":\"c1\",\"price\":1,\"quantity\":0.1,\"unitOfMeasurement\":\"kg\",\"description\":\"\"},{\"category\":\"c2\",\"price\":1,\"quantity\":0.1,\"unitOfMeasurement\":\"kg\",\"description\":\"\"}]}";

        var response = new ArrayList<Category>() {{
            add(new Category("c1", new BigDecimal(1), 0.1, "kg", ""));
            add(new Category("c2", new BigDecimal(1), 0.1, "kg", ""));
        }};

        given(requestService.getAllRecycleCategories()).willReturn(response);

        var result = mockMvc.perform(get(ENDPOINT_GET_CATEGORIES).header("Authorization", "Bearer 12334"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)))
                .andExpect(jsonPath("message", is(CommonConstant.Message.SUCCESSFUL_REQUEST)))
                .andReturn();

        Assertions.assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }

    @Test
    public void givenRecyclingCategoriesRequest_returnEmptyResponse() throws Exception {
        initAuth();
        String expectedResponse = "{\"returnCode\":\"00\",\"message\":\"The request has been successfully processed\",\"categories\":[]}";

        var response = new ArrayList<Category>();

        given(requestService.getAllRecycleCategories()).willReturn(response);

        var result = mockMvc.perform(get(ENDPOINT_GET_CATEGORIES).header("Authorization", "Bearer 12334"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)))
                .andExpect(jsonPath("message", is(CommonConstant.Message.SUCCESSFUL_REQUEST)))
                .andReturn();

        Assertions.assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }


    @Test
    public void givenRetrieveRequest_returnSuccess() throws Exception {
        initAuth();
        String expectedResponse = "{\"returnCode\":\"00\",\"message\":\"The request has been successfully processed\",\"data\":[]}";

        List<RecycleRequest> recycleRequests = new ArrayList<>();
        given(requestService.getRecycleRequests()).willReturn(recycleRequests);

        var result = mockMvc.perform(get(ENDPOINT_RETRIEVE_REQUEST).header("Authorization", "Bearer 12334"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)))
                .andExpect(jsonPath("message", is(CommonConstant.Message.SUCCESSFUL_REQUEST)))
                .andReturn();

        Assertions.assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }
}
