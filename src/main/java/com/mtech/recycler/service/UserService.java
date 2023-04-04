package com.mtech.recycler.service;

import com.mtech.recycler.entity.User;
import com.mtech.recycler.model.RegisterRequest;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(String id);

    User getUserByUserName(String userName);

    User createUser(RegisterRequest registerRequest);
}
