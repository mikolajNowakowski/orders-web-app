package com.app.data.repository.user;

import com.app.data.repository.generic.CrudRepository;
import com.app.model.user.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);
}
