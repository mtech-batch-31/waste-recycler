package com.mtech.recycler.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {

    private String status;
    private String returnCode; //todo: check why errorCode and errorDescription changed
    private String message;

}
