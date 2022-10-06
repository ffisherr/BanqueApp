package com.example.banque.user;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "User accountcreation operation")
    @PostMapping
    public UserResponse createUser(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @ApiOperation(value = "Fetching account details by id")
    @GetMapping("/{id}")
    public UserResponse userDetails(@PathVariable Long id) {
        return userService.userDetails(id);
    }
}
