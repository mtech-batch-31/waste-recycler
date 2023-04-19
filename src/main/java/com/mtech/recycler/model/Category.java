package com.mtech.recycler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private String name;

    private BigDecimal price;

    private int quantity;

    private String unitOfMeasurement;

}
