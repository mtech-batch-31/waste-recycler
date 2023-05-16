package com.mtech.recycler.service;

import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.model.*;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Optional<PricingResponse> getRequestTotalPricing(PricingRequest request);

    List<Category> getAllRecycleCategories();

    Optional<RecycleResponse> submitRequest(RecycleRequestDto recycleRequestDto);

    List<RecycleRequest> getRecycleRequests();
}
