package com.mtech.recycler.service;


import com.mtech.recycler.dto.RegisterRequestDto;
import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.notification.NotificationChannelFactory;
import com.mtech.recycler.repository.CustomerRepository;
import com.mtech.recycler.repository.VerificationTokenRepository;
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
    private final String email = "test@mail.com";
    private RegisterRequestDto registerRequestDto;

    @BeforeEach
    public void init() {
        VerificationTokenRepository verificationTokenRepository;
        NotificationChannelFactory notifyChannelFactory;

        registerRequestDto = new RegisterRequestDto();
        registerRequestDto.setEmail(email);
        String password = "P@ssw0rd";
        registerRequestDto.setPassword(password);
        registerRequestDto.setFirstName("John");
        registerRequestDto.setLastName("Doe");
        registerRequestDto.setContactNumber("87654321");
        registerRequestDto.setAddress("221B Baker Street, Bishan");
        registerRequestDto.setPostalCode("123456");
        userService = Mockito.mock(UserServiceImpl.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        verificationTokenRepository = Mockito.mock(VerificationTokenRepository.class);
        notifyChannelFactory = Mockito.mock(NotificationChannelFactory.class);
        userService = new UserServiceImpl(customerRepository, verificationTokenRepository, notifyChannelFactory);
    }


    @Test
    void testCreateUser_Success() {
        Customer customer = userService.createCustomer(registerRequestDto);
        Mockito.verify(customerRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        Assertions.assertNotNull(customer);
        Assertions.assertEquals(customer.getEmail(), email);
    }

    @Test
    void testCreateUser_missingEmail() {
        registerRequestDto.setEmail(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_invalidEmail() {
        registerRequestDto.setEmail("test@mail");
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_emailAlreadyExists() {
        Mockito.when(customerRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(Optional.of(new Customer()));
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_missingPassword() {
        registerRequestDto.setPassword(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_invalidPassword() {
        registerRequestDto.setPassword("password");
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_missingFirstName() {
        registerRequestDto.setFirstName(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_missingLastName() {
        registerRequestDto.setLastName(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_missingContactNumber() {
        registerRequestDto.setContactNumber(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_invalidContactNumber() {
        registerRequestDto.setContactNumber("12345");
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_missingAddress() {
        registerRequestDto.setAddress(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_missingPostalCode() {
        registerRequestDto.setPostalCode(null);
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testCreateUser_invalidPostalCode() {
        registerRequestDto.setPostalCode("12345");
        Assertions.assertThrows(ResponseStatusException.class, () -> userService.createCustomer(registerRequestDto));
    }

    @Test
    void testGetUserByEmail_ReturnSpecificCustomer() {
        String expectedEmail = "test@test.com";
        String expectedPassword = "pass123";

        var customer = new Customer();
        customer.setEmail("test@test.com");
        customer.setPassword("pass123");

        Mockito.when(customerRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(customer));

        var result = userService.getUserByEmail("test@test.com");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedEmail, result.getEmail());
        Assertions.assertEquals(expectedPassword, result.getPassword());

    }

    @Test
    void testGetUserByEmail_ThrowsExceptionWhenEmpty() {
        Mockito.when(customerRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> userService.getUserByEmail(""));
    }
}
