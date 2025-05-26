package com.saas.subscription_platform.controller;

import com.saas.subscription_platform.dto.RegisterRequest;
import com.saas.subscription_platform.dto.AuthRequest;
import com.saas.subscription_platform.dto.AuthResponse;
import com.saas.subscription_platform.service.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }
}
