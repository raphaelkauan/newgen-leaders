package com.newgenleaders.modules.post.dto;

import jakarta.validation.constraints.NotNull;

public record PostRequestDto(
        @NotNull(message = "Campo obrigatório!")
        String title,

        @NotNull(message = "Campo obrigatório!")
        String content) {
}
