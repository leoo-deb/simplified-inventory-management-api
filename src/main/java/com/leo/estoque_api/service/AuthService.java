package com.leo.estoque_api.service;

import com.leo.estoque_api.config.security.JwtService;
import com.leo.estoque_api.dto.auth.AuthRequest;
import com.leo.estoque_api.dto.auth.AuthResponse;
import com.leo.estoque_api.dto.user.UserMapper;
import com.leo.estoque_api.dto.user.UserResponse;
import com.leo.estoque_api.exceptions.UserNotFoundException;
import com.leo.estoque_api.model.User;
import com.leo.estoque_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest authRequest) {
        String normalizeEmail = authRequest.email().trim().toLowerCase(Locale.ROOT);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizeEmail, authRequest.password())
        );

        User user = userRepository.findByEmail(authRequest.email())
                .orElseThrow(() -> new UserNotFoundException(normalizeEmail));

        UserResponse userResponse = mapper.toUserDTO(user);
        String token = jwtService.generateToken(user);

        return new AuthResponse(token, "Bearer", userResponse);
    }

}
