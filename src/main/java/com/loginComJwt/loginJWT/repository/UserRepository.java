package com.loginComJwt.loginJWT.repository;

import com.loginComJwt.loginJWT.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
}
