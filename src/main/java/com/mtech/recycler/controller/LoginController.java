package com.mtech.recycler.controller;

import com.mtech.recycler.model.LoginRequest;
import com.mtech.recycler.model.LoginResponse;
import com.mtech.recycler.model.User;
import com.mtech.recycler.repository.UserRepository;
import com.mtech.recycler.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/login")
public class LoginController {

    @Autowired
    private UserService service;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("reached");
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> user = service.getUserById("1");
        return ResponseEntity.ok(user);
    }
}
