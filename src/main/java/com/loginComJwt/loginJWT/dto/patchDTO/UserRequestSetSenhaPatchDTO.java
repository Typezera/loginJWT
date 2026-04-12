package com.loginComJwt.loginJWT.dto.patchDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestSetSenhaPatchDTO(
        @NotBlank(message = "Senha invalida")
        @Size(min = 5, max = 10, message = "Informe uma senha que tenha no minimo 5 caracteres ou no máximo 10")
        String senha
) {
}
