package com.mtech.recycler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private String category;

    private double quantity;

    private BigDecimal unitPrice;

    private BigDecimal subTotalPrice;
}
