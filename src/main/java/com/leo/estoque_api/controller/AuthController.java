package com.leo.estoque_api.controller;

import com.leo.estoque_api.dto.auth.AuthRequest;
import com.leo.estoque_api.dto.auth.AuthResponse;
import com.leo.estoque_api.dto.common.ApiResponseWrapper;
import com.leo.estoque_api.dto.user.UserRequest;
import com.leo.estoque_api.dto.user.UserResponse;
import com.leo.estoque_api.service.AuthService;
import com.leo.estoque_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(
                ApiResponseWrapper.success(authResponse, "Login successfully effected.")
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseWrapper<UserResponse>> registerUser(@Valid @RequestBody UserRequest request) {
        UserResponse userResponse = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.success((userResponse), "User created successfully."));
    }

}
