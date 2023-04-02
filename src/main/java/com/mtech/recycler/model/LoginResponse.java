package com.mtech.recycler.model;

import com.mtech.recycler.model.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginResponse extends BaseResponse {
    private String token;
}
