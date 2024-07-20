package com.newgenleaders.modules.user.controller;

import com.newgenleaders.modules.user.dto.UserProfileDto;
import com.newgenleaders.modules.user.dto.UserRequestDto;
import com.newgenleaders.modules.user.dto.UserResponseDto;
import com.newgenleaders.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register-user")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.registerUser(userRequestDto);
    }

    @GetMapping("/profile/{user_id}")
    public ResponseEntity<UserProfileDto> userProfile(@PathVariable(value = "user_id") UUID userId) {
        return userService.userProfile(userId);
    }
}
