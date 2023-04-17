package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.model.Category;
import com.mtech.recycler.model.PricingRequest;
import com.mtech.recycler.model.PricingResponse;
import com.mtech.recycler.model.RecyclingCategoryResponse;
import com.mtech.recycler.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/request")
public class RequestController {

    private final RequestService requestService;

    @Autowired
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
}
