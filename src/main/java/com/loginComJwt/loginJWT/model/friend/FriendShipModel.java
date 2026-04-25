package com.loginComJwt.loginJWT.model.friend;

import com.loginComJwt.loginJWT.model.user.UserModel;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "friendship")
public class FriendShipModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserModel sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserModel receiver;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    private LocalDateTime createdAt;


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public void setReceiver(UserModel receiver) {
        this.receiver = receiver;
    }

    public void setSender(UserModel sender) {
        this.sender = sender;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public UserModel getReceiver() {
        return receiver;
    }

    public UserModel getSender() {
        return sender;
    }
}
