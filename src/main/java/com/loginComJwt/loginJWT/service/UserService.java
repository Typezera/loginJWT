package com.loginComJwt.loginJWT.service;

import com.loginComJwt.loginJWT.auth.dto.UserLoginRequestDTO;
import com.loginComJwt.loginJWT.auth.dto.UserLoginResponseDTO;
import com.loginComJwt.loginJWT.auth.dto.UserRequestDTO;
import com.loginComJwt.loginJWT.auth.dto.UserResponseDTO;
import com.loginComJwt.loginJWT.auth.service.JwtService;
import com.loginComJwt.loginJWT.dto.*;
import com.loginComJwt.loginJWT.dto.patchDTO.*;
import com.loginComJwt.loginJWT.model.UserModel;
import com.loginComJwt.loginJWT.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        user.setActivate(true);


        UserModel usuario = userRepository.save(user);

        return new UserResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone()
        );
    }

    public UserLoginResponseDTO login(UserLoginRequestDTO user){
        var usuario =  userRepository.findByEmailAndActivateTrue(user.email())
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
        var usuarios = userRepository.findAllByActivateTrue();

        return usuarios.stream().map(usuario -> new UserResponseGetDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail()
        ))
                .toList();
    };

    public UserResponseGetDTO encontrarUsuarioEmail(String email){
        return userRepository.findByEmailAndActivateTrue(email)
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
        return userRepository.findByIdActivateTrue(id)
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
        var user = userRepository.findByIdActivateTrue(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente não encontrado"
                ));

        validarUsuarioLogado(user);

        user.setNome(name.nome());
        var atualizarUsuario = userRepository.save(user);
        return new UserResponseGetNamePatchDTO(
                atualizarUsuario.getNome()
        );
    }

    public UserResponseGetEmailPatchDTO atualizarEmail(Long id, UserRequestSetEmailPatchDTO email){
        var user = userRepository.findByIdActivateTrue(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente não encontrado"
                ));
        validarUsuarioLogado(user);

        user.setEmail(email.email());

        var atualizaEmail = userRepository.save(user);

        return new UserResponseGetEmailPatchDTO(
                atualizaEmail.getEmail()
        );
    }

    public void atualizarSenha(Long id, UserRequestSetSenhaPatchDTO senha){
        var user = userRepository.findByIdActivateTrue(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente não encontrado"
                ));

        validarUsuarioLogado(user);
        user.setSenha(passwordEncoder.encode(senha.senha()));
        userRepository.save(user);
    }

    @Transactional
    public void desativarUsuario(Long id){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente não encontrado"
                ));
        validarUsuarioLogado(user);
        user.setActivate(false);
    }


    private UserModel getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailLogado = (String) authentication.getPrincipal();

        return userRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuário do token não encontrado."
                ));
    }

    private void validarUsuarioLogado(UserModel usuario){
        UserModel userLogado = getUsuarioLogado();

        if (!usuario.getId().equals(userLogado.getId())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Você não pode atualizar informações de outro usuário"
            );
        }
    }

}
