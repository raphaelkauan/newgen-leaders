package com.newgenleaders.modules.post.dto;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

public class FeedItemDto extends RepresentationModel<FeedItemDto> {
    private UUID postId;
    private String title;
    private String content;
    private String username;

    public FeedItemDto(UUID postId, String title, String content, String username) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.username = username;
    }

    public UUID getPostId() {
        return postId;
    }

    public void setPostId(UUID postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
