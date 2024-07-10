package com.newgenleaders.modules.post.controller;

import com.newgenleaders.modules.post.dto.PostRequestDto;
import com.newgenleaders.modules.post.dto.PostResponseDto;
import com.newgenleaders.modules.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto postRequestDto, JwtAuthenticationToken jwtAuthenticationToken) {
        return this.postService.createPost(postRequestDto, jwtAuthenticationToken);
    }

}
