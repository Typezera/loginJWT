package com.loginComJwt.loginJWT.controller;

import com.loginComJwt.loginJWT.dto.UserRequestSetNamePatchDTO;
import com.loginComJwt.loginJWT.dto.UserResponseGetDTO;
import com.loginComJwt.loginJWT.dto.UserResponseGetNamePatchDTO;
import com.loginComJwt.loginJWT.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("{id}")
    public ResponseEntity<UserResponseGetNamePatchDTO> atualizarNome(
            @PathVariable Long id,
            @RequestBody UserRequestSetNamePatchDTO name){
        return ResponseEntity.ok(userService.atualizarNome(id,name));
    }
}
