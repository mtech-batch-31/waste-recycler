package com.mtech.recycler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecycleResponse extends PricingResponse {
    private String returnCode;
    private String collectionStatus;
    private String promoCode;
    private String contactPerson;
    private String contactNumber;
    private String collectionDate;
}





