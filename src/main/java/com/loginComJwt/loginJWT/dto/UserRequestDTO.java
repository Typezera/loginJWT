package com.loginComJwt.loginJWT.dto;

import jakarta.validation.constraints.*;


public record UserRequestDTO(
         @NotBlank( message = "Nome não pode ser vazio.")
         String nome,
         @NotBlank( message = "Email não pode ser vazio.")
         @Email(message = "informe um email valido.")
         String email,
         @NotBlank(message = "Informe uma senha.")
         @Size(min = 5, max = 10, message = "A senha precisa no minimo de 5 caracteres")
         String senha,
         @NotBlank(message = "Informe um telefone.")
         @Pattern(
                 regexp = "^\\\\(\\\\d{2}\\\\)\\\\s\\\\d{4,5}-\\\\d{4}$",
                 message = "Formato de telefone inválido. Use: (XX) XXXXX-XXXX"
         )
         String telefone
) {
}
