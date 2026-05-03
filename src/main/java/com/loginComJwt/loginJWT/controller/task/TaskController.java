package com.loginComJwt.loginJWT.controller.task;

import com.loginComJwt.loginJWT.dto.taskDto.TaskRequestDTO;
import com.loginComJwt.loginJWT.dto.taskDto.TaskResponseDTO;
import com.loginComJwt.loginJWT.dto.taskDto.dtoPatch.updateTaskRequestDTO;
import com.loginComJwt.loginJWT.dto.taskDto.dtoPatch.updateTaskResponseDTO;
import com.loginComJwt.loginJWT.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){this.taskService = taskService;}

    @Operation(
            summary = "Trás todas as tarefas do usuário logado.",
            description = "Exibe apenas as tarefas que pertencem a aquela usuário logado."
    )
    @GetMapping()// trás as tarefas do proprio usuário
    public ResponseEntity<List<TaskResponseDTO>> verTarefas(){
        return ResponseEntity.ok(taskService.mostrarTarefas());
    }

    @Operation(
            summary = "cria uma tarefa.",
            description = "Apenas um usuário logado pode criar uma tarefa."
    )
    @PostMapping("/create")
    public ResponseEntity<TaskResponseDTO> criarTarefas(
            @Valid @RequestBody TaskRequestDTO taskRequestDTO){
        return ResponseEntity.ok(taskService.criarTarefa(taskRequestDTO));
    };

    @Operation(
            summary = "Atualzia a descrição da tarefa.",
            description = "Apenas o dono da tarefa pode atualizar."
    )
    @PatchMapping("/update/{id}")
    public ResponseEntity<updateTaskResponseDTO> updateDescricao(
            @Valid
            @PathVariable Long id,
            @RequestBody updateTaskRequestDTO descricao){
        return ResponseEntity.ok(taskService.atualizarDescricaoTarefa(id, descricao));
    }

    @Operation(
            summary = "Deleta uma tarefa pelo ID",
            description = "É possivel remover uma tarefa pelo ID, apenas o dono da tarefa pode remove-la"
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removerTarefa(
            @PathVariable Long id
    ){
        taskService.deletarTarefa(id);
        return ResponseEntity.ok("Tarefa removida com sucesso!");
    }
}
