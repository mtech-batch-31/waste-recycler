package com.mtech.recycler.controller;

import com.mtech.recycler.model.BaseResponse;
import com.mtech.recycler.model.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    @GetMapping("/secured")
    public ResponseEntity<BaseResponse> test() {
        return ResponseEntity.ok(new BaseResponse("hello", null, null));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest registerRequest) {
        log.info("registerRequest: {}", registerRequest);
        return ResponseEntity.ok(new BaseResponse("success", null, null));
    }
}
