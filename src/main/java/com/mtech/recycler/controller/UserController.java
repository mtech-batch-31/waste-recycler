package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.helper.Logger;
import com.mtech.recycler.model.RegisterRequest;
import com.mtech.recycler.model.base.BaseResponse;
import com.mtech.recycler.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final Logger log = Logger.getInstance();

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/secured")
    public ResponseEntity<BaseResponse> test() {
        return ResponseEntity.ok(new BaseResponse("00", null));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest request) {
        userService.createCustomer(request);
        return ResponseEntity.ok(new BaseResponse("00", CommonConstant.Message.SUCCESSFUL_REQUEST));
    }

    @PostMapping("/registrationConfirm")
    public ResponseEntity<BaseResponse> registrationConfirm(@RequestParam("token") String token) {
        log.info("RegistrationConfirm Verification Token:" + token);
        boolean result = userService.registrationConfirm(token);
        return ResponseEntity.ok(new BaseResponse("00", CommonConstant.Message.SUCCESSFUL_REQUEST));
    }
}
