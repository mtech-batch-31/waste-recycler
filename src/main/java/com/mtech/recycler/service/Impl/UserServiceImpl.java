package com.mtech.recycler.service.Impl;

import com.mtech.recycler.model.User;
import com.mtech.recycler.repository.UserRepository;
import com.mtech.recycler.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(String id) {
        if (!StringUtils.hasText(id))
            throw new IllegalArgumentException("id cannot be null");

        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUserName(String userName) {
        if (!StringUtils.hasText(userName))
            throw new IllegalArgumentException("user name cannot be null");

        return userRepository.findUserByUserName(userName);
    }
}
