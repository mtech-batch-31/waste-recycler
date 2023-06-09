package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.dto.LoginRequestDto;
import com.mtech.recycler.dto.LoginResponseDto;
import com.mtech.recycler.helper.Logger;
import com.mtech.recycler.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class LoginController {

    private final Logger log = Logger.getInstance();

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("reached");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("LoginController - authenticate - started");

        Optional<LoginResponseDto> response = loginService.authenticate(loginRequestDto);

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
