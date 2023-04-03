package com.mtech.recycler.service.Impl;

import com.mtech.recycler.entity.User;
import com.mtech.recycler.exception.UserNotFoundException;
import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.model.Role;
import com.mtech.recycler.repository.UserRepository;
import com.mtech.recycler.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserById(String id) {
        if (!StringUtils.hasText(id))
            throw new IllegalArgumentException("id cannot be null");

        return userRepository.findById(id);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName).orElseThrow(() -> new UserNotFoundException(String.format("The user name (%s) cannot be found", userName)));
    }

    @Override
    public User createUser(RegisterRequest registerRequest) {
        log.info("creating new user");
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        user.setFirstname(registerRequest.getFirstName());
        user.setLastname(registerRequest.getLastName());
        user.setRole(Role.CUSTOMER);
        return userRepository.save(user);
    }
}
