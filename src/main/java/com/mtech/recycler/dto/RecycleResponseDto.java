package com.mtech.recycler.dto;

import com.mtech.recycler.dto.base.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecycleResponseDto extends BaseResponseDto implements Serializable {
    private String email;
    private BigDecimal totalPrice;
    private List<ItemDto> items;
    private String collectionStatus;
    private String promoCode;
    private String contactPerson;
    private String contactNumber;
    private String collectionDate;
}





