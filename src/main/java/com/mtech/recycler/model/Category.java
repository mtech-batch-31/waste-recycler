package com.mtech.recycler.model;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class Category {

    @NotNull(message = "name is empty")
    private String category;

    private BigDecimal price;

    @Min(message = "Quantity must be greater than 0", value = 1)
    private double quantity;

    private String unitOfMeasurement;

}
