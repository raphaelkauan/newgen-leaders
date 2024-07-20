package com.newgenleaders.modules.user.service;

import com.newgenleaders.common.exception.UserConflictException;
import com.newgenleaders.common.exception.UserInvalid;
import com.newgenleaders.modules.post.dto.PostDto;
import com.newgenleaders.modules.post.entity.PostEntity;
import com.newgenleaders.modules.post.repository.PostRepository;
import com.newgenleaders.modules.role.entity.RoleEntity;
import com.newgenleaders.modules.role.repository.RoleRepository;
import com.newgenleaders.modules.user.dto.*;
import com.newgenleaders.modules.user.entity.UserEntity;
import com.newgenleaders.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final RabbitTemplate rabbitTemplate;
    private final PostRepository postRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, RabbitTemplate rabbitTemplate, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.postRepository = postRepository;
    }

    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        Optional<UserEntity> usernameValidation = userRepository.findByUsername(userRequestDto.username());
        Optional<UserEntity> emailValidation = userRepository.findByEmail(userRequestDto.email());

        if(usernameValidation.isPresent() || emailValidation.isPresent()) {
            throw new UserConflictException("Esse nome de usuário ou email já existem.");
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
        Map<String, Object> msg = new HashMap<>();
        msg.put("pattern", "v1.queue-mail");
        msg.put("data", mailDto);
        rabbitTemplate.convertAndSend("v1.queue-mail", msg);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
    }

    public ResponseEntity<UserProfileDto> userProfile(UUID userId) {
        Optional<UserEntity> findUser = userRepository.findById(userId);

        if(findUser.isEmpty()) {
            throw new UserInvalid("Esse usuário não existe.");
        }

        UserEntity userEntity = findUser.get();
        UserDto userDto = new UserDto(userEntity.getIdUser(), userEntity.getUsername(), userEntity.getEmail());

        List<PostEntity> posts = postRepository.findByUserEntityUserId(userId);
        List<PostDto> postDto = posts.stream()
                .map(post -> new PostDto(post.getTitle(), post.getContent()))
                .toList();

        UserProfileDto userProfileDto = new UserProfileDto(userDto, postDto);

        return ResponseEntity.status(HttpStatus.OK).body(userProfileDto);
    }
}
