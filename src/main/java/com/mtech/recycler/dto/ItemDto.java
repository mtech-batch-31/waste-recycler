package com.mtech.recycler.dto;

import com.mtech.recycler.constant.CommonConstant;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    @NotNull(message = CommonConstant.ErrorMessage.CATEGORY_VALIDATION_FAILED)
    @NotEmpty(message = CommonConstant.ErrorMessage.CATEGORY_VALIDATION_FAILED)
    private String category;

    @DecimalMin(message = CommonConstant.ErrorMessage.QUANTITY_VALIDATION_FAILED, value = "0.1")
    private double quantity;

    private BigDecimal unitPrice;

    private BigDecimal subTotalPrice;

    private String description;
}
