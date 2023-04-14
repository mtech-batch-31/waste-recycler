package com.mtech.recycler.model;

import com.mtech.recycler.model.base.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingResponse extends BaseResponse {

    private BigDecimal totalPrice;

    private List<Items> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Items {
        private String name;

        private int quantity;

        private BigDecimal itemPrice;
    }
}




