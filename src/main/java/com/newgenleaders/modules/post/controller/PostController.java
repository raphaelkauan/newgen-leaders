package com.newgenleaders.modules.post.controller;

import com.newgenleaders.modules.post.dto.FeedDto;
import com.newgenleaders.modules.post.dto.PostDto;
import com.newgenleaders.modules.post.dto.PostRequestDto;
import com.newgenleaders.modules.post.dto.PostResponseDto;
import com.newgenleaders.modules.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
            ) {
        return this.postService.feed(page, pageSize);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Object> getPost(@PathVariable(value = "id") UUID id) {
        return this.postService.getPost(id);
    }
}
