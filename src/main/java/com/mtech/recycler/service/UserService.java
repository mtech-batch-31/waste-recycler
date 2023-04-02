package com.mtech.recycler.service;

import com.mtech.recycler.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(String id);

}
