package com.mtech.recycler.service;

import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.dto.*;

import java.util.List;
import java.util.Optional;

public interface RequestService {

    Optional<PricingResponseDto> getRequestTotalPricing(PricingRequestDto request);

    List<CategoryDto> getAllRecycleCategories();

    Optional<RecycleResponseDto> submitRequest(RecycleRequestDto recycleRequestDto);

    List<RecycleRequest> getRecycleRequests();
}
