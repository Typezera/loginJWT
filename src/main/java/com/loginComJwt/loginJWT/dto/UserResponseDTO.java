package com.loginComJwt.loginJWT.dto;

public record UserResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone
) {
}
