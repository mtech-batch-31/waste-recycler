package com.mtech.recycler.model;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitRequest extends PricingRequest {

    private String returnCode;
    private String collectionStatus;
    private String promoCode;
    private String contactPerson;
    private String contactNumber;
    private String collectionDate;
    private List<Category> data;

}






