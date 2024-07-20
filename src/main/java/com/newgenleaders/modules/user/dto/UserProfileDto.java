package com.newgenleaders.modules.user.dto;

import com.newgenleaders.modules.post.dto.PostDto;

import java.util.List;

public record UserProfileDto(UserDto userDto, List<PostDto> postDto) {
}
