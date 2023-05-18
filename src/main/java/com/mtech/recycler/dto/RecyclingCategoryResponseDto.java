package com.mtech.recycler.dto;

import com.mtech.recycler.dto.base.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecyclingCategoryResponseDto extends BaseResponseDto implements Serializable {
    private List<CategoryDto> categories;
}
