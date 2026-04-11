package com.loginComJwt.loginJWT.service;

import com.loginComJwt.loginJWT.auth.dto.UserLoginRequestDTO;
import com.loginComJwt.loginJWT.auth.dto.UserLoginResponseDTO;
import com.loginComJwt.loginJWT.auth.dto.UserRequestDTO;
import com.loginComJwt.loginJWT.auth.dto.UserResponseDTO;
import com.loginComJwt.loginJWT.auth.filter.JwtFilter;
import com.loginComJwt.loginJWT.auth.service.JwtService;
import com.loginComJwt.loginJWT.dto.*;
import com.loginComJwt.loginJWT.model.UserModel;
import com.loginComJwt.loginJWT.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    //{metodo de criação de usuário, vai ser chamado no controller: [POST]}
    public UserResponseDTO criarUsuario(UserRequestDTO userRequest){
        UserModel user = new UserModel();
        user.setNome(userRequest.nome());
        user.setEmail(userRequest.email());
        user.setSenha(passwordEncoder.encode(userRequest.senha()));
        user.setTelefone(userRequest.telefone());



        UserModel usuario = userRepository.save(user);

        return new UserResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO user){
        var usuario =  userRepository.findByEmail(user.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário com o Email: " + user.email() + " não encontrado."
                ));

        if (!passwordEncoder.matches(user.senha(), usuario.getSenha())){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Senha inválida"
            );
        }

        String token = jwtService.generateToken(usuario);
        return new UserLoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        );
    }

    public List<UserResponseGetDTO> exibirUsuarios() {
        var usuarios = userRepository.findAll();

        return usuarios.stream().map(usuario -> new UserResponseGetDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        ))
                .toList();
    };

    public UserResponseGetDTO encontrarUsuarioEmail(String email){
        return userRepository.findByEmail(email)
                .map(user ->
                        new UserResponseGetDTO(
                                user.getId(),
                                user.getNome(),
                                user.getEmail()
                        )
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente não encontrado"
                ));
    }

    public UserResponseGetDTO encontrarUsuarioById(Long id) {
        return userRepository.findById(id)
                .map( user ->
                        new UserResponseGetDTO(
                                user.getId(),
                                user.getNome(),
                                user.getEmail()
                        )
                )
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Esse cliente não existe."
                ));
    }

    public UserResponseGetNamePatchDTO atualizarNome(Long id, UserRequestSetNamePatchDTO name){

        return userRepository.findById(id)
                .map( user -> {
                    user.setNome(name.nome());
                    var atualizaNome = userRepository.save(user);
                    return new UserResponseGetNamePatchDTO(
                            atualizaNome.getNome()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente Inexistente."
                ));
    }

    public UserResponseGetEmailPatchDTO atualizarEmail(Long id, UserRequestSetEmailPatchDTO email){
        return userRepository.findById(id)
                .map( user -> {
                    user.setEmail(email.email());
                    var atualizaEmail = userRepository.save(user);
                    return new UserResponseGetEmailPatchDTO(
                            atualizaEmail.getEmail()
                    );
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente não encontrado"
                ));
    }

}
