package com.app.service.user;

import com.app.model.dto.user.CreateUserDto;
import com.app.model.dto.user.GetUserDto;

public interface UserService  {
    GetUserDto register(CreateUserDto createUserDto);

    GetUserDto activate(Long userId, Long expirationTime);

}
