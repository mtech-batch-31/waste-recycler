package com.mtech.recycler.controller;

import com.mtech.recycler.model.LoginRequest;
import com.mtech.recycler.model.LoginResponse;
import com.mtech.recycler.model.User;
import com.mtech.recycler.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/login")
public class LoginController {

    private final UserRepository repository;

    @Autowired
    public LoginController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<LoginResponse> test() {
        return ResponseEntity.ok(new LoginResponse("testing"));
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Optional<User> user = repository.findById("123");
        return ResponseEntity.ok(new LoginResponse("testing"));
    }
}
