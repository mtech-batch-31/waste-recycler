package com.mtech.recycler.model;

import com.mtech.recycler.model.base.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecycleResponse extends BaseResponse implements Serializable {
    private String email;
    private BigDecimal totalPrice;
    private List<Item> items;
    private String collectionStatus;
    private String promoCode;
    private String contactPerson;
    private String contactNumber;
    private String collectionDate;
}





