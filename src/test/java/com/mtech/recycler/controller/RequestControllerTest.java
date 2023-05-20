package com.mtech.recycler.controller;

import com.mtech.recycler.config.JwtTokenProvider;
import com.mtech.recycler.config.SecurityConfig;
import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.dto.CategoryDto;
import com.mtech.recycler.dto.ItemDto;
import com.mtech.recycler.dto.PricingRequestDto;
import com.mtech.recycler.dto.PricingResponseDto;
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

@WebMvcTest(RequestController.class)
@Import(SecurityConfig.class)
class RequestControllerTest {

    private final String ENDPOINT_CALCULATE_PRICING = "/api/v1/request/price";

    private final String ENDPOINT_GET_CATEGORIES = "/api/v1/request/categories";

    private final String ENDPOINT_RETRIEVE_REQUEST = "/api/v1/request/retrieve";

    @Autowired
    private MockMvc mockMvc;

    private PricingRequestDto request;

    @MockBean
    private RequestServiceImpl requestService;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private UserServiceImpl userService;


    @BeforeEach
    void setupEach() {
        List<ItemDto> itemDtos = new ArrayList<>() {{
            add(new ItemDto("Electronics", 700, new BigDecimal(0.1), new BigDecimal(0), ""));
            add(new ItemDto("Battery", 500, new BigDecimal(1), new BigDecimal(0), ""));
        }};

        request = new PricingRequestDto("promo", itemDtos);

    }

    private void initAuth() {
        given(tokenProvider.validateToken(any(String.class))).willReturn(true);
        given(tokenProvider.getUserNameFromJWT(any(String.class))).willReturn("user");
        given(userService.getUserByEmail(any(String.class))).willReturn(new User());
        given(tokenProvider.getRoleFromJWT(any(String.class))).willReturn("test");
    }

    @Test
    void givenRecyclingRequestWhenSendForPricing_returnSuccessfulResponse() throws Exception {
        initAuth();
        String expectedResponse = "{\"returnCode\":\"00\",\"message\":\"The request has been successfully processed\",\"totalPrice\":100,\"items\":[{\"category\":\"test\",\"quantity\":1.0,\"unitPrice\":50,\"subTotalPrice\":50,\"description\":\"\"},{\"category\":\"test2\",\"quantity\":1.0,\"unitPrice\":50,\"subTotalPrice\":50,\"description\":\"\"}]}";
        String requestJsonString = Utilities.asJsonString(request);
        List<ItemDto> itemDtos = new ArrayList<>() {{
            add(new ItemDto("test", 1, new BigDecimal(50), new BigDecimal(50), ""));
            add(new ItemDto("test2", 1, new BigDecimal(50), new BigDecimal(50), ""));
        }};
        PricingResponseDto response = new PricingResponseDto(new BigDecimal(100), itemDtos);

        given(requestService.getRequestTotalPricing(any(PricingRequestDto.class))).willReturn(Optional.of(response));

        MvcResult result = mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(CommonConstant.ReturnCode.SUCCESS)))
                .andExpect(jsonPath("message", is(CommonConstant.Message.SUCCESSFUL_REQUEST)))
                .andReturn();

        Assertions.assertEquals(expectedResponse, result.getResponse().getContentAsString());
    }

    @Test
    void givenRecyclingRequestWhenSendWithoutCategory_return404Response() throws Exception {
        initAuth();
        request.getData().get(0).setCategory("");
        String requestJsonString = Utilities.asJsonString(request);

        mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("returnCode", is(String.valueOf(HttpStatus.BAD_REQUEST.value()))))
                .andExpect(jsonPath("message", is(CommonConstant.ErrorMessage.CATEGORY_VALIDATION_FAILED)));

    }

    @Test
    void givenRecyclingRequestWhenQuantityIsLessOrEqualZero_return404Response() throws Exception {
        initAuth();
        request.getData().get(0).setQuantity(0);
        String requestJsonString = Utilities.asJsonString(request);

        mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("returnCode", is(String.valueOf(HttpStatus.BAD_REQUEST.value()))))
                .andExpect(jsonPath("message", is(CommonConstant.ErrorMessage.QUANTITY_VALIDATION_FAILED)));

    }

    @Test
    void givenRecyclingRequestWhenEmptyObjectFromService_return404Response() throws Exception {
        initAuth();
        String requestJsonString = Utilities.asJsonString(request);

        given(requestService.getRequestTotalPricing(any(PricingRequestDto.class))).willReturn(Optional.empty());

        mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenRecyclingRequestWhenThrowException_return404Response() throws Exception {
        initAuth();
        String requestJsonString = Utilities.asJsonString(request);

        given(requestService.getRequestTotalPricing(any(PricingRequestDto.class))).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "error"));

        mockMvc.perform(post(ENDPOINT_CALCULATE_PRICING).content(requestJsonString).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer 12334").with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("returnCode", is(String.valueOf(HttpStatus.BAD_REQUEST.value()))))
                .andExpect(jsonPath("message", is("error")));
    }

    @Test
    void givenRecyclingCategoriesRequest_returnSuccessfulResponse() throws Exception {
        initAuth();
        String expectedResponse = "{\"returnCode\":\"00\",\"message\":\"The request has been successfully processed\",\"categories\":[{\"category\":\"c1\",\"price\":1,\"unitOfMeasurement\":\"kg\",\"description\":\"\"},{\"category\":\"c2\",\"price\":1,\"unitOfMeasurement\":\"kg\",\"description\":\"\"}]}";

        var response = new ArrayList<CategoryDto>() {{
            add(new CategoryDto("c1", new BigDecimal(1),  "kg", ""));
            add(new CategoryDto("c2", new BigDecimal(1),  "kg", ""));
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
    void givenRecyclingCategoriesRequest_returnEmptyResponse() throws Exception {
        initAuth();
        String expectedResponse = "{\"returnCode\":\"00\",\"message\":\"The request has been successfully processed\",\"categories\":[]}";

        var response = new ArrayList<CategoryDto>();

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
    void givenRetrieveRequest_returnSuccess() throws Exception {
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
