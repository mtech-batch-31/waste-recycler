package com.mtech.recycler.service;


import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.repository.CustomerRepository;
import com.mtech.recycler.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Slf4j
@SpringBootTest
@Disabled
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void testCreateUser_Success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword("123");
        registerRequest.setEmail("test@test.com");
        userService.createCustomer(registerRequest);
        verify(customerRepository, times(1)).save(any());
    }
}
