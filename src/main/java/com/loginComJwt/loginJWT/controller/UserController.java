package com.loginComJwt.loginJWT.controller;

import com.loginComJwt.loginJWT.dto.*;
import com.loginComJwt.loginJWT.dto.patchDTO.*;
import com.loginComJwt.loginJWT.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){this.userService = userService;}

    @GetMapping
    public ResponseEntity<List<UserResponseGetDTO>> verUsuarios(){
        return ResponseEntity.ok(userService.exibirUsuarios());
    }

    @GetMapping("email/{email}")
    public ResponseEntity<UserResponseGetDTO> buscarUsuarioEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.encontrarUsuarioEmail(email));
    }

    @GetMapping("id/{id}")
    public ResponseEntity<UserResponseGetDTO> buscarUsuarioById(@PathVariable Long id){
        return ResponseEntity.ok(userService.encontrarUsuarioById(id));
    }

    @PatchMapping("update/name/{id}")
    public ResponseEntity<UserResponseGetNamePatchDTO> atualizarNome(
            @PathVariable Long id,
            @RequestBody UserRequestSetNamePatchDTO name){
        return ResponseEntity.ok(userService.atualizarNome(id,name));
    }

    @PatchMapping("update/email/{id}")
    public ResponseEntity<UserResponseGetEmailPatchDTO> atualizarEmail(
            @PathVariable Long id,
            @RequestBody UserRequestSetEmailPatchDTO email){
        return ResponseEntity.ok(userService.atualizarEmail(id, email));
    }

    @PatchMapping("update/password/{id}")
    public ResponseEntity<String> atualizarSenha(
            @PathVariable Long id,
            @RequestBody UserRequestSetSenhaPatchDTO senha){
        userService.atualizarSenha(id, senha);
        return ResponseEntity.ok("Senha atualizada com sucesso");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> desativarUsuario(
            @PathVariable Long id
    ){
        userService.desativarUsuario(id);
        return ResponseEntity.ok("Usuário desativado com sucesso");
    }

    @PatchMapping("reactivate")
    public ResponseEntity<String> reactivarConta(
            @RequestBody UserRequestSetEmailPatchDTO email){
        userService.reativarUsuario(email);
        return ResponseEntity.ok("Usuário reactivado com sucesso");
    }

}
