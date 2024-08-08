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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    private final RabbitTemplate rabbitTemplate;
    private final PostRepository postRepository;

    @Value("${img.dir}")
    private String UPLOAD_DIR;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository, RabbitTemplate rabbitTemplate, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.postRepository = postRepository;
    }

    public ResponseEntity<UserResponseDto> registerUser(UserRequestDto userRequestDto) {
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

        UserResponseDto userResponseDto = new UserResponseDto("Usário criado com sucesso.");

        // Envia username e email para fila.
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
        UserDto userDto = new UserDto(
                userEntity.getIdUser(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getImg_url()
        );

        List<PostEntity> posts = postRepository.findByUserEntityUserId(userId);
        List<PostDto> postDto = posts.stream()
                .map(post -> new PostDto(post.getPostId(), post.getTitle(), post.getContent()))
                .toList();

        UserProfileDto userProfileDto = new UserProfileDto(userDto, postDto);

        return ResponseEntity.status(HttpStatus.OK).body(userProfileDto);
    }

    public ResponseEntity<UserResponseDto> userUpdate(UUID userId, UserUpdateDto userUpdateDto, JwtAuthenticationToken jwt) {
        Optional<UserEntity> findUser = userRepository.findById(userId);

        if(findUser.isEmpty()) {
            throw new UserInvalid("Esse usuário não existe.");
        }

        UserEntity userEntity = findUser.get();
        userEntity.setUsername(userUpdateDto.username());

        if(userEntity.getIdUser().equals(UUID.fromString(jwt.getName()))) {
            userRepository.save(userEntity);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserResponseDto("Você não tem permissão para atualizar esse usuário."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto("Usário criado com sucesso."));
    }

    public ResponseEntity<UserResponseDto> uploadImgProfile(MultipartFile file, JwtAuthenticationToken jwt) {
        if(file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UserResponseDto("Imagem vazia."));
        }

        try {
            int number = new Random().nextInt(10000);

            String filePath = this.UPLOAD_DIR + number + "_" + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            Optional<UserEntity> user = userRepository.findById(UUID.fromString(jwt.getName()));

            if(user.isPresent()) {
                if(!user.get().getImg_url().isEmpty()) {
                    String imgUrl = user.get().getImg_url();
                    Files.deleteIfExists(Path.of(imgUrl));
                }

                UserEntity userEntity = user.get();
                userEntity.setImg_url(filePath);
                userRepository.save(userEntity);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto("Imagem salva com sucesso."));

        } catch (IOException e) {
            throw new RuntimeException("Não foi possível fazer upload." + e);
        }

    }
}
