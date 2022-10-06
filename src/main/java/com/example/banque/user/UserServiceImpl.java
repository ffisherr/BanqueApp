package com.example.banque.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserResponse create(UserCreateRequest request) {
        validateRequest(request);
        final var entity = mapToEntity(request);
        final var saved = repository.save(entity);
        return mapToResponse(saved);
    }

    @Override
    public UserResponse userDetails(Long id) {
        final var userDetails = repository.findById(id).map(this::mapToResponse);
        return userDetails.orElseThrow(() -> new IllegalArgumentException("Wrong id"));
    }

    private void validateRequest(UserCreateRequest request) {
        if (request.initialAmount().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Initial amount should not be less then zero");
        if (request.name().isBlank())
            throw new IllegalArgumentException("Name should not be empty");
    }

    private UserEntity mapToEntity(UserCreateRequest request) {
        return UserEntity.builder()
                .name(request.name())
                .balance(request.initialAmount())
                .build();
    }

    private UserResponse mapToResponse(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .balance(entity.getBalance())
                .build();
    }
}
