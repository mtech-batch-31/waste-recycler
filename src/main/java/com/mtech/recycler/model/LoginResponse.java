package com.mtech.recycler.model;

import com.mtech.recycler.model.base.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResponse extends BaseResponse {
    private final String token;
}
