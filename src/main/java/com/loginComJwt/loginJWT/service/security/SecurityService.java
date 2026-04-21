package com.loginComJwt.loginJWT.service.security;

import com.loginComJwt.loginJWT.model.task.TaskModel;
import com.loginComJwt.loginJWT.model.user.UserModel;
import com.loginComJwt.loginJWT.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SecurityService {
    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserModel getUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailLogado = (String) authentication.getPrincipal();

        return userRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuário do token não encontrado."
                ));
    }

    public void validarUsuarioLogado(UserModel usuario){
        UserModel userLogado = getUsuarioLogado();

        if (!usuario.getId().equals(userLogado.getId())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Você não pode atualizar informações de outro usuário"
            );
        }
    }

    public void validarDonoTarefa(TaskModel task, UserModel user){
        if(!task.getUsuario().getId().equals(user.getId())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não pode Alterar essa tarefa."
            );
        }
    }


}
