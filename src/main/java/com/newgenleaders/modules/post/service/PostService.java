package com.newgenleaders.modules.post.service;

import com.newgenleaders.common.exception.PostLengthException;
import com.newgenleaders.common.exception.PostValidationException;
import com.newgenleaders.modules.post.controller.PostController;
import com.newgenleaders.modules.post.dto.*;
import com.newgenleaders.modules.post.entity.PostEntity;
import com.newgenleaders.modules.post.repository.PostRepository;
import com.newgenleaders.modules.user.entity.UserEntity;
import com.newgenleaders.modules.user.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

    public ResponseEntity<FeedDto> feed(int page, int pageSize) {

        var posts = postRepository.findAll(PageRequest.of(page, pageSize))
                .map(post -> {
                    FeedItemDto feedItemDto = new FeedItemDto(post.getPostId(), post.getTitle(), post.getContent(), post.getUserEntity().getUsername());
                    feedItemDto.add(linkTo(methodOn(PostController.class).getPost(post.getPostId())).withSelfRel());
                    return feedItemDto;
                });

        FeedDto feedDto = new FeedDto(posts.getContent(), page, pageSize);

        return  ResponseEntity.status(HttpStatus.OK).body(feedDto);
    }

    public ResponseEntity<Object> getPost(UUID id) {
        Optional<PostEntity> postEntity = postRepository.findById(id);

        if(postEntity.isEmpty()) {
            throw new PostValidationException("Esse post não existe.");
        }

        FeedItemDto feedItemDto = new FeedItemDto(
                postEntity.get().getPostId(),
                postEntity.get().getTitle(),
                postEntity.get().getContent(),
                postEntity.get().getUserEntity().getUsername()
        );
        feedItemDto.add(linkTo(methodOn(PostController.class).feed(0, 10)).withRel("feed"));

        return ResponseEntity.status(HttpStatus.OK).body(feedItemDto);
    }

    public ResponseEntity<?> deletePost(UUID id, JwtAuthenticationToken jwt) {
        Optional<UserEntity> user = userRepository.findById(UUID.fromString(jwt.getName()));

        Optional<PostEntity> post = postRepository.findById(id);

        if(post.isEmpty()) {
            throw new PostValidationException("Esse post não existe.");
        }

        if(user.get().getIdUser().equals(post.get().getUserEntity().getIdUser())) {
                postRepository.deleteById(id);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para deletar esse post.");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Post deletado com sucesso.");
    }
}
