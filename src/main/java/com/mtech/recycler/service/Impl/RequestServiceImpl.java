package com.mtech.recycler.service.Impl;

import com.mtech.recycler.model.PricingRequest;
import com.mtech.recycler.model.PricingResponse;
import com.mtech.recycler.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    @Override
    public Optional<PricingResponse> GetRequestTotalPricing(List<PricingRequest> request) {
        return Optional.empty();
    }
}
