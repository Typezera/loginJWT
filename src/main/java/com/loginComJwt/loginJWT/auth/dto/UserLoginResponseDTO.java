package com.loginComJwt.loginJWT.auth.dto;

public record UserLoginResponseDTO(String token, Long id, String nome, String email) {
}
