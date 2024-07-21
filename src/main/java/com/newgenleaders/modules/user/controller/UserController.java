package com.newgenleaders.modules.user.controller;

import com.newgenleaders.modules.user.dto.UserProfileDto;
import com.newgenleaders.modules.user.dto.UserRequestDto;
import com.newgenleaders.modules.user.dto.UserResponseDto;
import com.newgenleaders.modules.user.dto.UserUpdateDto;
import com.newgenleaders.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

    @PutMapping("/profile/settings/{user_id}")
    public ResponseEntity<?> userUpdate(@PathVariable(value = "user_id") UUID userId, @RequestBody @Valid UserUpdateDto userUpdateDto,
    JwtAuthenticationToken jwt) {
        return userService.userUpdate(userId, userUpdateDto, jwt);
    }
}
