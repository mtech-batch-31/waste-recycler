package com.mtech.recycler.service;

import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.model.*;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Optional<PricingResponse> getRequestTotalPricing(PricingRequest request);

    List<Category> GetAllRecycleCategories();

    Optional<RecycleResponse> SubmitRequest(com.mtech.recycler.model.RecycleRequest recycleRequest);

    List<RecycleRequest> getRecycleRequests();
}
