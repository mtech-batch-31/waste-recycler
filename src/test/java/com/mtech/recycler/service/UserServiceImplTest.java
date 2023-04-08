package com.mtech.recycler.service;


import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.repository.CustomerRepository;
import com.mtech.recycler.repository.UserRepository;
import com.mtech.recycler.service.Impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    private UserRepository userRepository;

    private CustomerRepository customerRepository;

    private final String password = "P@ssw0rd";
    private final String email = "test@mail.com";

    @BeforeEach
    public void init() {
        userService = Mockito.mock(UserServiceImpl.class);
        userRepository = Mockito.mock(UserRepository.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        userService = new UserServiceImpl(userRepository, customerRepository);
    }


    @Test
    void testCreateUser_Success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        Customer customer = userService.createCustomer(registerRequest);
        Mockito.verify (customerRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        Assertions.assertNotNull(customer);
        Assertions.assertEquals(customer.getEmail(), email);
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        Mockito.when(customerRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(new Customer()));
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }
}
