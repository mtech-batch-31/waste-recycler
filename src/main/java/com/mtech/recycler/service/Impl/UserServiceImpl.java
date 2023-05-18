package com.mtech.recycler.service.Impl;

import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.entity.VerificationToken;
import com.mtech.recycler.helper.Logger;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.dto.RegisterRequestDto;
import com.mtech.recycler.dto.Role;
import com.mtech.recycler.notification.EmailVerification;
import com.mtech.recycler.notification.NotificationChannel;
import com.mtech.recycler.notification.NotificationChannelFactory;
import com.mtech.recycler.notification.model.NotificationModel;
import com.mtech.recycler.repository.CustomerRepository;
import com.mtech.recycler.repository.VerificationTokenRepository;
import com.mtech.recycler.service.UserService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Logger log = Logger.getInstance();

    private final CustomerRepository customerRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private NotificationChannelFactory notifyChannelFactory;

    public UserServiceImpl(CustomerRepository customerRepository, VerificationTokenRepository verificationTokenRepository, NotificationChannelFactory notifyChannelFactory){
        this.customerRepository = customerRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.notifyChannelFactory = notifyChannelFactory;
    }
    @Override
    public User getUserByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The user name (%s) cannot be found", email)));
    }

    @Override
    public Customer createCustomer(RegisterRequestDto registerRequestDto) {
        log.info("creating new customer registerRequest: " + registerRequestDto);
        validateRegisterRequest(registerRequestDto);
        Customer customer = new Customer();
        customer.setEmail(registerRequestDto.getEmail());
        customer.setPassword(Utilities.encodePassword(registerRequestDto.getPassword()));
        customer.setFirstName(registerRequestDto.getFirstName());
        customer.setLastName(registerRequestDto.getLastName());
        customer.setRole(Role.CUSTOMER);
        customer.setContactNumber(registerRequestDto.getContactNumber());
        customer.setAddress(registerRequestDto.getAddress());
        customer.setPostalCode(registerRequestDto.getPostalCode());
        customer.setEnabled(false);
        customerRepository.save(customer);

        //generate verification token
        log.info("Generating Verification Token");
        VerificationToken token = new VerificationToken();
        token.setEmail(customer.getEmail());
        token.generateVerificationToken();
        verificationTokenRepository.save(token);

        //send verification email
        log.info("Sending Verification Email");
        NotificationChannel channel = notifyChannelFactory.notificationChannel(NotificationChannelFactory.CHANNEL_TYPE.SMTP);
        if(channel != null) {
            EmailVerification emailVerification = new EmailVerification(channel);
            NotificationModel notifModel = new NotificationModel();
            notifModel.setUser(customer);
            notifModel.setVerificationToken(token.getToken());
            emailVerification.send(notifModel);
        }
        log.info("customer created: " + customer);
        return customer;
    }

    @Override
    public boolean registrationConfirm(String token) {

        VerificationToken verificationToken =  verificationTokenRepository.findByToken(token)
                                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Token"));
        //update user account to enable
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime tokeExpiryDate = DateTime.parse(verificationToken.getExpiryDate(), formatter);
        DateTime now = DateTime.now();
        log.info("Token Expires Time "+tokeExpiryDate);

        if(tokeExpiryDate.isAfter(now)) {
            Customer customer = customerRepository.findByEmail(verificationToken.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Token"));
            customer.setEnabled(true);
            customerRepository.save(customer);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token has expires");
        }
        return true;
    }


    private void validateRegisterRequest(RegisterRequestDto registerRequestDto) {
        if (!StringUtils.hasText(registerRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email cannot be empty");
        }
        if (!Utilities.isValidEmail(registerRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        }
        Optional<Customer> customerFromDB = customerRepository.findByEmail(registerRequestDto.getEmail());
        if (customerFromDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        if (!StringUtils.hasText(registerRequestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be empty");
        }
        if (!Utilities.isValidPassword(registerRequestDto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
        }
        if (!StringUtils.hasText(registerRequestDto.getFirstName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name cannot be empty");
        }
        if (!StringUtils.hasText(registerRequestDto.getLastName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name cannot be empty");
        }
        if (!StringUtils.hasText(registerRequestDto.getContactNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contact number cannot be empty");
        }
        if (!Utilities.isValidContactNumber(registerRequestDto.getContactNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid contact number");
        }
        if (!StringUtils.hasText(registerRequestDto.getAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address cannot be empty");
        }
        if (!StringUtils.hasText(registerRequestDto.getPostalCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Postal code cannot be empty");
        }
        if (!Utilities.isValidPostalCode(registerRequestDto.getPostalCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid postal code");
        }
    }


}
