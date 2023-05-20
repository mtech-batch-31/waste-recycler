package com.mtech.recycler.controller;

import com.mtech.recycler.constant.CommonConstant;
import com.mtech.recycler.dto.RegisterRequestDto;
import com.mtech.recycler.helper.Logger;
import com.mtech.recycler.dto.base.BaseResponseDto;
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
    public ResponseEntity<BaseResponseDto> test() {
        return ResponseEntity.ok(new BaseResponseDto("00", null));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDto> register(@RequestBody RegisterRequestDto request) {
        userService.createCustomer(request);
        return ResponseEntity.ok(new BaseResponseDto("00", CommonConstant.Message.SUCCESSFUL_REQUEST));
    }

    @PostMapping("/registrationConfirm")
    public ResponseEntity<BaseResponseDto> registrationConfirm(@RequestParam("token") String token) {
        log.info("RegistrationConfirm Verification Token:" + token);
        userService.registrationConfirm(token);
        return ResponseEntity.ok(new BaseResponseDto("00", CommonConstant.Message.SUCCESSFUL_REQUEST));
    }
}
