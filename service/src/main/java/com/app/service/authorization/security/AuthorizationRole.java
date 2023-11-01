package com.app.service.authorization.security;

import com.app.model.user.Role;

public enum AuthorizationRole {
    USER,
    ADMIN,
    ALL,
    IS_AUTH;

    public static AuthorizationRole toAuthorizationRole(Role role){
        return switch (role){
            case ADMIN -> AuthorizationRole.ADMIN;
            case USER -> AuthorizationRole.USER;
        };
    }

}
