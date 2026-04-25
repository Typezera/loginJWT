package com.loginComJwt.loginJWT.service.friendship;

import com.loginComJwt.loginJWT.model.friend.FriendShipModel;
import com.loginComJwt.loginJWT.model.friend.FriendshipStatus;
import com.loginComJwt.loginJWT.model.user.UserModel;
import com.loginComJwt.loginJWT.repository.friend.FriendshipRepository;
import com.loginComJwt.loginJWT.service.security.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final SecurityService securityService;
    public FriendshipService(FriendshipRepository friendshipRepository, SecurityService securityService){
        this.friendshipRepository = friendshipRepository;
        this.securityService = securityService;
    }

    public void enviarSocilitacao(long receiverId){
        UserModel sender = securityService.getUsuarioLogado();

        UserModel receiver = securityService.buscarReceiver(receiverId);

        securityService.validarReceiverAndSender(sender, receiver);

        FriendShipModel friendship = new FriendShipModel();
        friendship.setSender(sender);
        friendship.setReceiver(receiver);
        friendship.setStatus(FriendshipStatus.PENDENTE);
        friendship.setCreatedAt(LocalDateTime.now());

        friendshipRepository.save(friendship);
    }

    public void aceitarSolicitacao(Long solicitacao){
        UserModel usuarioLogado = securityService.getUsuarioLogado();

        var pedido = friendshipRepository.findById(solicitacao)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitação não encontrada."
                ));

        if (!pedido.getReceiver().getId().equals(usuarioLogado.getId())){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não pode fazer isso"
            );
        }

        if (!pedido.getStatus().equals(FriendshipStatus.PENDENTE)){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Essa solicitação já foi processada."
            );
        }

        pedido.setStatus(FriendshipStatus.ACEITAR);
        friendshipRepository.save(pedido);
    }
}
