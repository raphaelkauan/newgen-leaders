package com.newgenleaders.modules.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
    @NotNull(message = "Campo obrigatório!")
    @Size(min = 2, max = 45, message = "O nome deve ter entre 2 e 45 caracteres.")
    String username,

    @NotNull(message = "Campo obrigatório!")
    @Email(message = "Email inválido!")
    String email,

    @NotNull(message = "Campo obrigatório!")
    String password
) {
}
