package com.newgenleaders.modules.post.service;

import com.newgenleaders.common.exception.PostLengthException;
import com.newgenleaders.common.exception.UserConflictException;
import com.newgenleaders.modules.post.dto.PostRequestDto;
import com.newgenleaders.modules.post.dto.PostResponseDto;
import com.newgenleaders.modules.post.entity.PostEntity;
import com.newgenleaders.modules.post.repository.PostRepository;
import com.newgenleaders.modules.user.entity.UserEntity;
import com.newgenleaders.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.UUID;

@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto postRequestDto, JwtAuthenticationToken jwtAuthenticationToken) {
        Optional<UserEntity> user = userRepository.findById(UUID.fromString(jwtAuthenticationToken.getName()));

        if(postRequestDto.content().length() > 3000) {
                throw new PostLengthException("Você atingiu o limite máximo de 3000 caracteres.");
        }

        PostEntity post = new PostEntity();
        post.setUserEntity(user.get());
        post.setTitle(postRequestDto.title());
        post.setContent(postRequestDto.content());

        postRepository.save(post);

        return ResponseEntity.status(HttpStatus.CREATED).body(new PostResponseDto("Post publicado com sucesso!"));
    }

}