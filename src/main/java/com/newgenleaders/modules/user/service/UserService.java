package com.newgenleaders.modules.user.service;

import com.newgenleaders.common.exception.UserConflictException;
import com.newgenleaders.modules.user.dto.UserRequestDto;
import com.newgenleaders.modules.user.dto.UserResponseDto;
import com.newgenleaders.modules.user.entity.UserEntity;
import com.newgenleaders.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        Optional<UserEntity> usernameValidation = userRepository.findByUsername(userRequestDto.username());

        if(usernameValidation.isPresent()) {
            throw new UserConflictException("Esse nome de usuário já existe!");
        }

        UserEntity user = new UserEntity();
        user.setUsername(userRequestDto.username());
        user.setEmail(userRequestDto.email());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDto.password()));

        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto(user.getUsername(), "Usário criado com sucesso!");

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }
}
