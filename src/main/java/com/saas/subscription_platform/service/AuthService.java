package com.saas.subscription_platform.service;

import com.saas.subscription_platform.dto.RegisterRequest;
import com.saas.subscription_platform.dto.AuthRequest;
import com.saas.subscription_platform.dto.AuthResponse;
import com.saas.subscription_platform.model.Role;
import com.saas.subscription_platform.model.User;
import com.saas.subscription_platform.repository.UserRepository;
import com.saas.subscription_platform.security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<AuthResponse> register(RegisterRequest request) {
    	if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use"); // or handle with custom exception
        }
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    public ResponseEntity<AuthResponse> login(AuthRequest request) {
        try {
    	authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        } catch (BadCredentialsException ex) {
        	return ResponseEntity.status(401).build();
        }
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        String token = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
