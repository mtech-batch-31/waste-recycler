package com.mtech.recycler.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingRequestDto {

    private String promoCode;

    @Valid
    private List<ItemDto> data;

}
