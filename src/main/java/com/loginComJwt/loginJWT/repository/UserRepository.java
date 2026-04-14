package com.loginComJwt.loginJWT.repository;

import com.loginComJwt.loginJWT.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

   Optional<UserModel> findByEmail(String email);

    List<UserModel> findAllByActivateTrue();
    Optional<UserModel> findByIdActivateTrue(Long id);
    Optional<UserModel> findByEmailAndActivateTrue(String email);
}
