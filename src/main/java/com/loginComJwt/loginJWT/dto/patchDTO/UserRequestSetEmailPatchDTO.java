package com.loginComJwt.loginJWT.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestSetEmailPatchDTO(
        @NotBlank(message = "Informe um email valido")
        @Email(message = "Formato de email invalido")
        String email
) {
}
