package com.app.model.user;


import com.app.model.dto.auth.AuthorizationDto;
import com.app.model.dto.user.GetUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private boolean enabled;

    public User withPassword(String newPassword) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(newPassword)
                .role(role)
                .enabled(enabled)
                .build();
    }

    public User withEnabled(boolean newEnabled){
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .enabled(newEnabled)
                .build();
    }

    public GetUserDto toGetUserDto(){
        return new GetUserDto(id,username,email);
    }

    public AuthorizationDto toAuthorizationDto(){
        return new AuthorizationDto(role);
    }

    public boolean hasPassword(String expectedPassword){
        return password.equals(expectedPassword);
    }


}
