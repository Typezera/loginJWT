package com.loginComJwt.loginJWT.dto.patchDTO;

import jakarta.validation.constraints.NotBlank;

public record UserRequestSetNamePatchDTO(
        @NotBlank(message = "Preencha o campo")
        String nome
) {
}
