package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.model.*;
import com.mtech.recycler.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @PostMapping("/categories")
    public ResponseEntity<?> getRequestTotalPricing(@RequestBody PricingRequest request) {
        log.info("RequestController - getRequestTotalPricing - started");

        log.info("RequestController - getRequestTotalPricing - discount code: " + request.getPromoCode());

        Optional<PricingResponse> response = requestService.GetRequestTotalPricing(request);

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

        List<Category> recyclingCategories = requestService.GetAllRecycleCategories()
                .stream()
                .map(r -> new Category(r.getName(), r.getPrice(), 0, r.getUnitOfMeasurement()))
                .toList();

        response.setCategories(recyclingCategories);
        response.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        response.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);

        log.info("RequestController - getAllCategories - end");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recycle")
    public ResponseEntity<?> getRecycleRequest() {

        RecycleResponse recycleResponse = new RecycleResponse();
        recycleResponse.setTotalPrice(new BigDecimal(1000));
        recycleResponse.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        recycleResponse.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);
        return ResponseEntity.ok(recycleResponse);
    }

    @PostMapping("/recycle")
    public ResponseEntity<?> submitRequest(@RequestBody SubmitRequest submitRequest) {
        log.info("RequestController - submitRequest with two params");
        //input validation
        requestService.SubmitRequest(submitRequest);
        return ResponseEntity.ok("ok");
    }
}
