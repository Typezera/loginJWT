package com.loginComJwt.loginJWT.dto.friendship;

import com.loginComJwt.loginJWT.dto.userDto.UserResponseGetDTO;
import com.loginComJwt.loginJWT.model.friend.FriendshipStatus;
import com.loginComJwt.loginJWT.model.user.UserModel;

import java.time.LocalDateTime;

public record FriendshipResponseDTO(LocalDateTime date, FriendshipStatus status, UserResponseGetDTO user) {
}
