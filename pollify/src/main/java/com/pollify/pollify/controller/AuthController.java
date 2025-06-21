package com.pollify.pollify.controller;


import com.pollify.pollify.dto.AuthRequest;
import com.pollify.pollify.dto.AuthResponse;
import com.pollify.pollify.dto.RegisterRequest;
import com.pollify.pollify.model.User;
import com.pollify.pollify.servis.UserService;
import com.pollify.pollify.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/pollify/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Login endpoint
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user);
                return ResponseEntity.ok(new AuthResponse(token));
            }
        }

        return ResponseEntity.status(401).build();
    }
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        return ResponseEntity.ok(userService.save(user));
    }
}
