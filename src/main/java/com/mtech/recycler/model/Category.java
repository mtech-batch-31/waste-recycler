package com.mtech.recycler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private String name;

    private BigDecimal price;

    private int quantity;

    private List<String> unitOfMeasurement;

}
