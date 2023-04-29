package com.mtech.recycler.model;

import com.mtech.recycler.entity.RecycleItem;
import com.mtech.recycler.model.base.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper=true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetRecycleReqResponse extends BaseResponse {

    List<RecycleItem> data;
}






