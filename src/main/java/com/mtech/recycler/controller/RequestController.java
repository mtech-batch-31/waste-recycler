package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.helper.Logger;
import com.mtech.recycler.model.*;
import com.mtech.recycler.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/request")
public class RequestController {

    private final Logger log = Logger.getInstance();

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

        List<Category> recyclingCategories = requestService.getAllRecycleCategories();

        response.setCategories(recyclingCategories);
        response.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        response.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);

        log.info("RequestController - getAllCategories - end");

        return ResponseEntity.ok(response);
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
    public ResponseEntity<?> submitRequest(@RequestBody RecycleRequestDto request) {
        Optional<RecycleResponse> recycleResponse = requestService.submitRequest(request);
        return ResponseEntity.ok(recycleResponse);
    }
}