package com.mtech.recycler.model;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class PricingRequest {

    private String promoCode;

    private List<Category> data;

}
