package com.loginComJwt.loginJWT.dto.taskDto.dtoPatch;

import com.loginComJwt.loginJWT.model.task.TarefaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record updateTaskRequestDTO(
        String descricao,
        String nome,
        TarefaStatus status
) { }
