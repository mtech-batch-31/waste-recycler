package com.mtech.recycler.dto;


import com.mtech.recycler.constant.CommonConstant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    @NotNull(message = CommonConstant.ErrorMessage.CATEGORY_VALIDATION_FAILED)
    @NotEmpty(message = CommonConstant.ErrorMessage.CATEGORY_VALIDATION_FAILED)
    private String category;

    private BigDecimal price;

    //@DecimalMin(message = CommonConstant.ErrorMessage.QUANTITY_VALIDATION_FAILED, value = "0.1")
    //private double quantity;

    private String unitOfMeasurement;

    private String description;
}
