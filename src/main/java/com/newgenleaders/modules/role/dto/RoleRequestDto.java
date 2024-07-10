package com.newgenleaders.modules.role.dto;

import jakarta.validation.constraints.NotNull;

public record RoleRequestDto(
        @NotNull(message = "Campo obrigatório!")
        String name
) {
}
