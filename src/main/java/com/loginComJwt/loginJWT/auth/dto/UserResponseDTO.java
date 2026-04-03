package com.loginComJwt.loginJWT.auth.dto;

public record UserResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone
) {
}
