package com.loginComJwt.loginJWT.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDTO(
        @NotBlank(message = "Informe um email.")
        @Email(message = "Informe um email valido.")
        String email,
        @NotBlank(message = "Senha invalida")
        String senha
) {
}
