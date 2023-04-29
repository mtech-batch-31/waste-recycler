package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.model.*;
import com.mtech.recycler.service.RequestService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/request")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/price")
    public ResponseEntity<?> getRequestTotalPricing(@Valid @RequestBody PricingRequest request) {
        log.info("RequestController - getRequestTotalPricing - started");

        log.info("RequestController - getRequestTotalPricing - discount code: " + request.getPromoCode());

        Optional<PricingResponse> response = requestService.getRequestTotalPricing(request);

        log.info("RequestController - GetRequestTotalPricing - Is Empty: " + response.isEmpty());

        if (response.isEmpty())
            return ResponseEntity.notFound().build();

        PricingResponse pricingResponse = response.get();

        pricingResponse.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        pricingResponse.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);

        log.info("RequestController - getRequestTotalPricing - end");

        return ResponseEntity.ok(pricingResponse);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        log.info("RequestController - getAllCategories - started");
        RecyclingCategoryResponse response = new RecyclingCategoryResponse();

        List<Category> recyclingCategories = requestService.GetAllRecycleCategories();

        response.setCategories(recyclingCategories);
        response.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        response.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);

        log.info("RequestController - getAllCategories - end");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recycle")
    public ResponseEntity<?> getRecycleRequest(@RequestBody GetRequest getRequest) {
        Optional<RecycleRequest> recycleItems = requestService.getRequest(getRequest.getEmail(), getRequest.getRecord());
        return ResponseEntity.ok(recycleItems);
    }


    @GetMapping("/retrieve")
    public ResponseEntity<?> getRecycleRequests() {
        List<RecycleRequest> recycleItems = requestService.getRecycleRequests();
        GetRecycleReqResponse getRecycleReqResponse = new GetRecycleReqResponse();
        getRecycleReqResponse.setData(recycleItems);
        getRecycleReqResponse.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        getRecycleReqResponse.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
        return ResponseEntity.ok(getRecycleReqResponse);
    }


    @PostMapping("/recycle")
    public ResponseEntity<?> submitRequest(@RequestBody com.mtech.recycler.model.RecycleRequest recycleRequest) {
        Optional<RecycleResponse> recycleResponse = requestService.SubmitRequest(recycleRequest);
        return ResponseEntity.ok(recycleResponse);
    }
}