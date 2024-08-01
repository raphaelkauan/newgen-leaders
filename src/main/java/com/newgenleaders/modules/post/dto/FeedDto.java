package com.newgenleaders.modules.post.dto;

import java.util.List;

public record FeedDto(List<FeedItemDto> postDto, int page, int pageSize) {
}
