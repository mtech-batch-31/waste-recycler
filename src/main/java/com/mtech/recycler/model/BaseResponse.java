package com.mtech.recycler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
public class BaseResponse {

    private String status;
    private String errorCode;
    private String errorDescription;

}
