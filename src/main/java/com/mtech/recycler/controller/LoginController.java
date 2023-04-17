package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.model.LoginRequest;
import com.mtech.recycler.model.LoginResponse;
import com.mtech.recycler.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/auth")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("reached");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request) {
        log.info("LoginController - authenticate - started");

        if (!StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword()))
            return ResponseEntity.badRequest().body(CommonConstant.ErrorMessage.INVALID_REQUEST);

        Optional<LoginResponse> response = loginService.authenticate(request.getEmail(), request.getPassword());

        log.info("LoginController - authenticate - Is Empty: " + response.isEmpty());

        if (response.isEmpty())
            return ResponseEntity.notFound().build();

        var loginResponse = response.get();

        loginResponse.setReturnCode(CommonConstant.ReturnCode.SUCCESS);
        loginResponse.setMessage(CommonConstant.Message.SUCCESSFUL_REQUEST);

        log.info("LoginController - authenticate - end");

        return ResponseEntity.ok(loginResponse);
    }
}
