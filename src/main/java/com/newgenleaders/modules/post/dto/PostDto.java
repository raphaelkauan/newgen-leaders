package com.newgenleaders.modules.post.dto;

import java.util.UUID;

public record PostDto(UUID id, String title, String content) {
}
