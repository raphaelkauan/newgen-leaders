package com.newgenleaders.modules.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
    @NotNull(message = "Campo obrigatório!")
    @Size(min = 2, max = 45, message = "O nome deve ter entre 2 e 45 caracteres.")
    String username
) {
}
