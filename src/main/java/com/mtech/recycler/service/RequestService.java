package com.mtech.recycler.service;

import com.mtech.recycler.model.PricingRequest;
import com.mtech.recycler.model.PricingResponse;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Optional<PricingResponse> GetRequestTotalPricing(List<PricingRequest> request);
}
