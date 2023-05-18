package com.mtech.recycler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private String category;

    private double quantity;

    private BigDecimal unitPrice;

    private BigDecimal subTotalPrice;

    private String description;
}
