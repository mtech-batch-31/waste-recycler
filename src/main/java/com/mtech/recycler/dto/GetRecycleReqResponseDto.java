package com.mtech.recycler.dto;

import com.mtech.recycler.entity.RecycleRequest;
import com.mtech.recycler.dto.base.BaseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper=true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRecycleReqResponseDto extends BaseResponseDto {

    List<RecycleRequest> data;
}






