package com.newgenleaders.modules.user.service;

import com.newgenleaders.common.exception.UserConflictException;
import com.newgenleaders.modules.role.entity.RoleEntity;
import com.newgenleaders.modules.role.repository.RoleRepository;
import com.newgenleaders.modules.user.dto.MailDto;
import com.newgenleaders.modules.user.dto.UserRequestDto;
import com.newgenleaders.modules.user.dto.UserResponseDto;
import com.newgenleaders.modules.user.entity.UserEntity;
import com.newgenleaders.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.management.relation.Role;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final RabbitTemplate rabbitTemplate;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        Optional<UserEntity> usernameValidation = userRepository.findByUsername(userRequestDto.username());

        if(usernameValidation.isPresent()) {
            throw new UserConflictException("Esse nome de usuário já existe!");
        }

        RoleEntity roleBasic = roleRepository.findByName(RoleEntity.Values.basic.name());

        UserEntity user = new UserEntity();
        user.setUsername(userRequestDto.username());
        user.setEmail(userRequestDto.email());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDto.password()));
        user.setRoleEntities(Set.of(roleBasic));

        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto(user.getUsername(), "Usário criado com sucesso!");

        MailDto mailDto = new MailDto(userRequestDto.username(), userRequestDto.email());
        rabbitTemplate.convertAndSend("v1.queue-mail", mailDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }
}
