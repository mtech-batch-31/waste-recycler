package com.mtech.recycler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecycleResponse {
    private String email;
    private String returnCode;
    private String message;
    private BigDecimal totalPrice;
    private List<Item> items;
    private String collectionStatus;
    private String promoCode;
    private String contactPerson;
    private String contactNumber;
    private String collectionDate;
}





