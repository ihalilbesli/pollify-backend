package com.pollify.pollify.servis;

import com.pollify.pollify.model.User;

import java.util.Optional;

public interface UserService {

    User save(User user); // kullanıcı kaydet

    Optional<User> findByEmail(String email); // email ile kullanıcı getir

    boolean existsByEmail(String email); // email daha önce kayıtlı mı

    Optional<User> findById(Long id); // id ile kullanıcı getir
}
