package com.mtech.recycler.dto;

import com.mtech.recycler.dto.base.BaseResponseDto;
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
public class PricingResponseDto extends BaseResponseDto {

    private BigDecimal totalPrice;

    private List<ItemDto> items;
}





