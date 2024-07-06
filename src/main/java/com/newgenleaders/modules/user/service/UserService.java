package com.newgenleaders.modules.user.service;

import com.newgenleaders.modules.user.dto.UserRequestDto;
import com.newgenleaders.modules.user.entity.UserEntity;
import com.newgenleaders.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {

        var user = new UserEntity();
        user.setUsername(userRequestDto.username());
        user.setEmail(userRequestDto.email());
        user.setPassword(userRequestDto.password());

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("usuário criado com sucesso!");
    }
}
