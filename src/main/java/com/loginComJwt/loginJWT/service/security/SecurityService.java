package com.loginComJwt.loginJWT.service.security;

import com.loginComJwt.loginJWT.model.task.TaskModel;
import com.loginComJwt.loginJWT.model.user.UserModel;
import com.loginComJwt.loginJWT.repository.friend.FriendshipRepository;
import com.loginComJwt.loginJWT.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SecurityService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public SecurityService(UserRepository userRepository, FriendshipRepository friendshipRepository){
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
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

    public UserModel buscarReceiver(Long receiver){
        UserModel rece =  userRepository.findByIdAndActivateTrue(receiver)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuario não encontrado"
                ));
        return rece;
    }

    public void validarReceiverAndSender(UserModel sender, UserModel receiver){
        if (sender.getId().equals(receiver.getId())){
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Não é possível adicionar a sí mesmo."
            );
        }

        if (friendshipRepository.existsBySenderAndReceiver(sender, receiver)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma solicitação em pendente."
            );
        }
        if (friendshipRepository.existsBySenderAndReceiver(receiver,sender)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Já existe uma solicitação em pendente"
            );
        }

    }


}
