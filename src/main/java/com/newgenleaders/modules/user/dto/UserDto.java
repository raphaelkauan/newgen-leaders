package com.newgenleaders.modules.user.dto;

import java.util.UUID;

public record UserDto(UUID userId, String username, String email) {
}
