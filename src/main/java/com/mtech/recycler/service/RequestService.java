package com.mtech.recycler.service;

import com.mtech.recycler.entity.RecycleItem;
import com.mtech.recycler.model.*;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Optional<PricingResponse> GetRequestTotalPricing(PricingRequest request);

    List<Category> GetAllRecycleCategories();

    Optional<RecycleResponse> SubmitRequest(RecycleRequest recycleRequest);

    Optional<RecycleItem> getRequest(String email,int record);
}
