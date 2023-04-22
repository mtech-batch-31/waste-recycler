package com.mtech.recycler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper=true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecycleRequest extends PricingRequest {
    private String email;
    private String returnCode;
    private String collectionStatus;
    private String contactPerson;
    private String contactNumber;
    private String collectionDate;
}






