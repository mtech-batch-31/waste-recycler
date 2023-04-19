package com.mtech.recycler.service;


import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.repository.CustomerRepository;
import com.mtech.recycler.service.Impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
public class UserServiceImplTest {

    private UserService userService;


    private CustomerRepository customerRepository;

    private final String password = "P@ssw0rd";
    private final String email = "test@mail.com";
    private RegisterRequest registerRequest;

    @BeforeEach
    public void init() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setContactNumber("87654321");
        registerRequest.setAddress("221B Baker Street, Bishan");
        registerRequest.setPostalCode("123456");
        userService = Mockito.mock(UserServiceImpl.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        userService = new UserServiceImpl(customerRepository);
    }


    @Test
    void testCreateUser_Success() {
        Customer customer = userService.createCustomer(registerRequest);
        Mockito.verify(customerRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        Assertions.assertNotNull(customer);
        Assertions.assertEquals(customer.getEmail(), email);
    }

    @Test
    void testCreateUser_missingEmail() {
        registerRequest.setEmail(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_invalidEmail() {
        registerRequest.setEmail("test@mail");
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_emailAlreadyExists() {
        Mockito.when(customerRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(new Customer()));
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_missingPassword() {
        registerRequest.setPassword(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_invalidPassword() {
        registerRequest.setPassword("password");
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_missingFirstName() {
        registerRequest.setFirstName(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_missingLastName() {
        registerRequest.setLastName(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_missingContactNumber() {
        registerRequest.setContactNumber(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_invalidContactNumber() {
        registerRequest.setContactNumber("12345");
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_missingAddress() {
        registerRequest.setAddress(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_missingPostalCode() {
        registerRequest.setPostalCode(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }

    @Test
    void testCreateUser_invalidPostalCode() {
        registerRequest.setPostalCode("12345");
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequest));
    }


}
