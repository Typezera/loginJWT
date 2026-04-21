package com.loginComJwt.loginJWT.dto.taskDto.dtoPatch;

import jakarta.validation.constraints.NotBlank;

public record DescricaoRequestPatchDto(
        @NotBlank(message = "Insira uma descrição.")
        String descricao
) { }
