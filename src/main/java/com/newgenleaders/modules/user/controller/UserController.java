package com.newgenleaders.modules.user.controller;

import com.newgenleaders.modules.user.dto.UserRequestDto;
import com.newgenleaders.modules.user.dto.UserResponseDto;
import com.newgenleaders.modules.user.repository.UserRepository;
import com.newgenleaders.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }
}
