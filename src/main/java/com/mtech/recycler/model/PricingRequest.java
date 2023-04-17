package com.mtech.recycler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingRequest {

    private String promoCode;

    private List<Category> categories;

}
