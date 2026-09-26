package com.leo.estoque_api.service;

import com.leo.estoque_api.dto.user.UserMapper;
import com.leo.estoque_api.dto.user.UserRequest;
import com.leo.estoque_api.dto.user.UserResponse;
import com.leo.estoque_api.dto.user.UserUpdateRequest;
import com.leo.estoque_api.exceptions.ConflictException;
import com.leo.estoque_api.exceptions.UserNotFoundException;
import com.leo.estoque_api.model.User;
import com.leo.estoque_api.model.enums.UserRole;
import com.leo.estoque_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(mapper::toUserDTO);
    }

    @Transactional
    public UserResponse register(UserRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new ConflictException(
                    String.format("User with e-mail: %s already exist.", request.email()));
        }

        String normalizeEmail = request.email().trim().toLowerCase(Locale.ROOT);
        User user = mapper.toUser(request);

        user.setEmail(normalizeEmail);
        user.setPassword(
                passwordEncoder.encode(request.password())
        );

        user.setActive(true);
        user.setEmailVerified(false);
        user.setCreatedAt(OffsetDateTime.now());

        if (request.role() == null) {
            user.setRole(UserRole.COSTUMER);
        } else {
            user.setRole(request.role());
        }

        User saved = userRepository.save(user);
        return mapper.toUserDTO(saved);
    }

    @Transactional
    public UserResponse update(UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(request.name());
        String normalizeEmail = request.email().trim().toLowerCase(Locale.ROOT);

        if (!request.email().equals(user.getEmail()) && userRepository.existsByEmailIgnoreCase(normalizeEmail)) {
            throw new ConflictException(
                    String.format("User with e-mail: %s already exist.", request.email()));
        }

        user.setEmail(normalizeEmail);
        user.setEmailVerified(false);

        return mapper.toUserDTO(user);
     }

}
