package com.mtech.recycler.model;

import lombok.Data;

@Data
public class RegisterRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String address;
    private String postalCode;
}
