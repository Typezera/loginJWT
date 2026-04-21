package com.loginComJwt.loginJWT.dto.taskDto.dtoPatch;

import com.loginComJwt.loginJWT.model.task.TarefaStatus;

public record updateTaskResponseDTO(String descricao, String nome, TarefaStatus status) { }
