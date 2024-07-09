package com.newgenleaders.modules.security.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        String username,

        @NotNull(message = "Campo obrigatório!")
        String password) {
}
