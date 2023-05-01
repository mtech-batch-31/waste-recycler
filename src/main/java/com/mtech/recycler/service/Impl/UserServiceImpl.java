package com.mtech.recycler.service.Impl;

import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.entity.User;
import com.mtech.recycler.entity.VerificationToken;
import com.mtech.recycler.helper.Utilities;
import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.model.Role;
import com.mtech.recycler.notification.EmailVerification;
import com.mtech.recycler.notification.NotificationChannel;
import com.mtech.recycler.notification.NotificationChannelFactory;
import com.mtech.recycler.notification.model.NotificationModel;
import com.mtech.recycler.repository.CustomerRepository;
import com.mtech.recycler.repository.VerificationTokenRepository;
import com.mtech.recycler.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;


import java.text.SimpleDateFormat;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final CustomerRepository customerRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private NotificationChannelFactory notifyChannelFactory;

    public UserServiceImpl(CustomerRepository customerRepository, VerificationTokenRepository verificationTokenRepository){
        this.customerRepository = customerRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }
    @Override
    public User getUserByEmail(String userName) {
        return customerRepository.findByEmail(userName).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The user name (%s) cannot be found", userName)));
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
        EmailVerification emailVerification = new EmailVerification(channel);
        NotificationModel notifModel = new NotificationModel();
        notifModel.setUser(customer);
        notifModel.setVerificationToken(token.getToken());
        emailVerification.send(notifModel);

        log.info("customer created: {}", customer);
        return customer;
    }

    @Override
    public boolean registrationConfirm(String token) {

        VerificationToken verificationToken =  verificationTokenRepository.findByToken(token)
                                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Invalid Token")));
        //update user account to enable
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime tokeExpiryDate = DateTime.parse(verificationToken.getExpiryDate(), formatter);
        DateTime now = DateTime.now();
        log.info("Token Expires Time "+tokeExpiryDate);

        if(tokeExpiryDate.isAfter(now)) {
            Customer customer = customerRepository.findByEmail(verificationToken.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Invalid Token")));
            customer.setEnabled(true);
            customerRepository.save(customer);
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Token has expires"));
        }
        return true;
    }


    private void validateRegisterRequest(RegisterRequest registerRequest) {
        log.info("validating registerRequest: {}", registerRequest);
        if (!StringUtils.hasText(registerRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email cannot be empty");
        }
        if (!Utilities.isValidEmail(registerRequest.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid email");
        }
        Optional<Customer> customerFromDB = customerRepository.findByEmail(registerRequest.getEmail());
        if (customerFromDB.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }
        if (!StringUtils.hasText(registerRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password cannot be empty");
        }
        if (!Utilities.isValidPassword(registerRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid Password");
        }
        if (!StringUtils.hasText(registerRequest.getFirstName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "firstName cannot be empty");
        }
        if (!StringUtils.hasText(registerRequest.getLastName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lastName cannot be empty");
        }
        if (!StringUtils.hasText(registerRequest.getContactNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "contactNumber cannot be empty");
        }
        if (!Utilities.isValidContactNumber(registerRequest.getContactNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid contactNumber");
        }
        if (!StringUtils.hasText(registerRequest.getAddress())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "address cannot be empty");
        }
        if (!StringUtils.hasText(registerRequest.getPostalCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "postalCode cannot be empty");
        }
        if (!Utilities.isValidPostalCode(registerRequest.getPostalCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid postalCode");
        }
    }


}
