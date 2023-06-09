package com.mtech.recycler.dto;

import com.mtech.recycler.constant.CommonConstant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotNull(message = CommonConstant.ErrorMessage.WRONG_USER_NAME_OR_PASSWORD)
    private String email;

    @NotNull(message = CommonConstant.ErrorMessage.WRONG_USER_NAME_OR_PASSWORD)
    private String password;
}

