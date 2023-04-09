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
    public User getUserByEmail(String userName) {
        return userRepository.findUserByEmail(userName).orElseThrow(() -> new UserNotFoundException(String.format("The user name (%s) cannot be found", userName)));
    }

    @Override
    public Customer createCustomer(RegisterRequest registerRequest) {
        log.info("creating new customer registerRequest: {}", registerRequest);
        validateRegisterRequest(registerRequest);
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


    private void validateRegisterRequest(RegisterRequest registerRequest) {
        log.info("validating registerRequest: {}", registerRequest);
        if(!StringUtils.hasText(registerRequest.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email cannot be empty");
        }
        if(!Utilities.isValidEmail(registerRequest.getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid email");
        }
        Optional<Customer> customerFromDB = customerRepository.findByEmail(registerRequest.getEmail());
        if(customerFromDB.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }
        if(!StringUtils.hasText(registerRequest.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password cannot be empty");
        }
        if(!Utilities.isValidPassword(registerRequest.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid Password");
        }
        if(!StringUtils.hasText(registerRequest.getFirstName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "firstName cannot be empty");
        }
        if(!StringUtils.hasText(registerRequest.getLastName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lastName cannot be empty");
        }
        if(!StringUtils.hasText(registerRequest.getContactNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contactNumber cannot be empty");
        }
        if(!Utilities.isValidContactNumber(registerRequest.getContactNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid contactNumber");
        }
        if(!StringUtils.hasText(registerRequest.getAddress())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "address cannot be empty");
        }
        if(!StringUtils.hasText(registerRequest.getPostalCode())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "postalCode cannot be empty");
        }
        if(!Utilities.isValidPostalCode(registerRequest.getPostalCode())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid postalCode");
        }
    }


}
