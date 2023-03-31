package com.mtech.recycler.model;

import lombok.Data;

@Data
public class LoginRequest {
    private final String userName;
    private final String password;
}
