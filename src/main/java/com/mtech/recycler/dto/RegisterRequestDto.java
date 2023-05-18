package com.mtech.recycler.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String address;
    private String postalCode;
}
