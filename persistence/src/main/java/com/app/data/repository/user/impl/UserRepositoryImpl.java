package com.app.data.repository.user.impl;

import com.app.data.repository.generic.AbstractCrudRepository;
import com.app.data.repository.user.UserRepository;
import com.app.model.user.User;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends AbstractCrudRepository<User,Long> implements UserRepository {
    public UserRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    @Override
    public Optional<User> findByUserName(String username) {
        var sql = "select * from users where username = :username";
        return  jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("username",username)
                .mapToBean(User.class)
                .findFirst()
        );

    }

    @Override
    public Optional<User> findByEmail(String email) {
        var sql = "select * from users where email = :email";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("email",email)
                .mapToBean(User.class)
                .findFirst()
        );
    }
}
