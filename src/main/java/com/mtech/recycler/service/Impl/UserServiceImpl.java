package com.mtech.recycler.service.Impl;

import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.exception.UserNotFoundException;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.model.Role;
import com.mtech.recycler.repository.CustomerRepository;
import com.mtech.recycler.repository.UserRepository;
import com.mtech.recycler.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    @Override
    public Optional<User> getUserById(String id) {
        if (!StringUtils.hasText(id))
            throw new IllegalArgumentException("id cannot be null");

        return userRepository.findById(id);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userRepository.findUserByEmail(userName).orElseThrow(() -> new UserNotFoundException(String.format("The user name (%s) cannot be found", userName)));
    }

    @Override
    public Customer createCustomer(RegisterRequest registerRequest) {
        log.info("creating new customer registerRequest: {}", registerRequest);
        Optional<Customer> customerFromDB = customerRepository.findByEmail(registerRequest.getEmail());
        if(customerFromDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        Customer customer = new Customer();
        customer.setEmail(registerRequest.getEmail());
        customer.setPassword(Utilities.encodePassword(registerRequest.getPassword()));
        customer.setFirstName(registerRequest.getFirstName());
        customer.setLastName(registerRequest.getLastName());
        customer.setRole(Role.CUSTOMER);
        customer.setContactNumber(registerRequest.getContactNumber());
        customer.setAddress(registerRequest.getAddress());
        customer.setPostalCode(registerRequest.getPostalCode());
        customerRepository.save(customer);
        log.info("customer created: {}", customer);
        return customer;
    }
}
