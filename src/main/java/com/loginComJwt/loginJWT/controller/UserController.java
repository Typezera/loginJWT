package com.loginComJwt.loginJWT.controller;

import com.loginComJwt.loginJWT.dto.UserResponseGetDTO;
import com.loginComJwt.loginJWT.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
