package com.loginComJwt.loginJWT.controller.friendship;

import com.loginComJwt.loginJWT.service.friendship.FriendshipService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/pedido")
public class FriendshipController {
    private final FriendshipService friendshipService;
    public FriendshipController(FriendshipService friendshipService){
        this.friendshipService = friendshipService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("{id}")
    public ResponseEntity<String> adicionarUsuario(
            @PathVariable Long id
    ){
        friendshipService.enviarSocilitacao(id);
        return ResponseEntity.ok("Solicitação enviada com sucesso.");
    }
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}/aceitar")
    public ResponseEntity<String> aceitarSolicitacao(
            @PathVariable Long id
    ){
        friendshipService.aceitarSolicitacao(id);
        return ResponseEntity.ok("Solicitação aceita.");
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/{id}/recusar")
    public ResponseEntity<String> recusarSolicitacao(
            @PathVariable Long id
    ){
        friendshipService.recusarSolicitacao(id);
        return ResponseEntity.ok("Solicitação recusada.");
    }
}
