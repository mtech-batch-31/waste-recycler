package com.mtech.recycler.service;

import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.dto.RegisterRequestDto;

public interface UserService {

    User getUserByEmail(String userName);

    Customer createCustomer(RegisterRequestDto registerRequestDto);

    boolean registrationConfirm(String token);
}
