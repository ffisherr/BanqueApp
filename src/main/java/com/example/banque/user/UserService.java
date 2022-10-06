package com.example.banque.user;

public interface UserService {

    UserResponse create(UserCreateRequest request);

    UserResponse userDetails(Long id);
}
