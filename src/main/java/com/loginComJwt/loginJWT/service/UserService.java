package com.loginComJwt.loginJWT.service;

import com.loginComJwt.loginJWT.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


}
