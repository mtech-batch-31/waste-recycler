package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.entity.Customer;
import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.model.base.BaseResponse;
import com.mtech.recycler.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/secured")
    public ResponseEntity<BaseResponse> test() {
        return ResponseEntity.ok(new BaseResponse("00", null));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest registerRequest) {
        Customer customer = userService.createCustomer(registerRequest);
        return ResponseEntity.ok(new BaseResponse("00", CommonConstant.Message.SUCCESSFUL_REQUEST));
    }

    @PostMapping("/registrationConfirm")
    public ResponseEntity<BaseResponse> registrationConfirm(@RequestParam("token") String token) {
        log.info("RegistrationConfirm Verification Token"+token);
        boolean result = userService.registrationConfirm(token);
        return ResponseEntity.ok(new BaseResponse("00", CommonConstant.Message.SUCCESSFUL_REQUEST));
    }
}
