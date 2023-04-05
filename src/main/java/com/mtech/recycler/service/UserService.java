package com.mtech.recycler.service;

import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.model.RegisterRequest;

public interface UserService {

    User getUserByEmail(String userName);

    Customer createCustomer(RegisterRequest registerRequest);
}
