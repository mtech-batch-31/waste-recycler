package com.mtech.recycler.controller;

import com.mtech.recycler.entity.User;
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
        return ResponseEntity.ok(new BaseResponse("hello", null, null));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest registerRequest) {
        log.info("registerRequest: {}", registerRequest);
        User user = userService.createUser(registerRequest);
        log.info("user created: {}", user);
        return ResponseEntity.ok(new BaseResponse("success", null, null));
    }
}
