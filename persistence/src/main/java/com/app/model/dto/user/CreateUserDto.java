package com.app.model.dto.user;

import com.app.model.user.Role;
import com.app.model.user.User;

public record CreateUserDto(String username,
                            String password,
                            String passwordConfirmation,
                            String email,
                            Role role
                            ) {

    public User toUser(){
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .enabled(false)
                .build();
    }

}
