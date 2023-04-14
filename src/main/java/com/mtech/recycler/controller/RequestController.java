package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.model.PricingRequest;
import com.mtech.recycler.model.PricingResponse;
import com.mtech.recycler.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public ResponseEntity<?> getRequestTotalPricing(@RequestBody List<PricingRequest> request) {
        log.info("RequestController - getRequestTotalPricing - started");

        Optional<PricingResponse> response = requestService.GetRequestTotalPricing(request);

        log.info("RequestController - GetRequestTotalPricing - Is Empty: " + response.isEmpty());

//        if (response.isEmpty())
//            return ResponseEntity.notFound().build();

//        PricingResponse pricingResponse = response.get();

        List<PricingResponse.Items> items = new ArrayList<>();
        items.add(new PricingResponse.Items(request.get(0).getName(), request.get(0).getQuantity(), new BigDecimal(500)));
        items.add(new PricingResponse.Items(request.get(1).getName(), request.get(1).getQuantity(), new BigDecimal(500)));

        PricingResponse pricingResponse = new PricingResponse();
        pricingResponse.setTotalPrice(new BigDecimal(1000));
        pricingResponse.setItems(items);

        pricingResponse.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        pricingResponse.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);

        log.info("RequestController - getRequestTotalPricing - end");

        return ResponseEntity.ok(pricingResponse);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories(@RequestBody List<PricingRequest> request) {
        log.info("RequestController - getRequestTotalPricing - started");

        Optional<PricingResponse> response = requestService.GetRequestTotalPricing(request);

        log.info("RequestController - GetRequestTotalPricing - Is Empty: " + response.isEmpty());

//        if (response.isEmpty())
//            return ResponseEntity.notFound().build();

//        PricingResponse pricingResponse = response.get();

        List<PricingResponse.Items> items = new ArrayList<>();
        items.add(new PricingResponse.Items(request.get(0).getName(), request.get(0).getQuantity(), new BigDecimal(500)));
        items.add(new PricingResponse.Items(request.get(1).getName(), request.get(1).getQuantity(), new BigDecimal(500)));

        PricingResponse pricingResponse = new PricingResponse();
        pricingResponse.setTotalPrice(new BigDecimal(1000));
        pricingResponse.setItems(items);

        pricingResponse.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        pricingResponse.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);

        log.info("RequestController - getRequestTotalPricing - end");

        return ResponseEntity.ok(pricingResponse);
    }
}
