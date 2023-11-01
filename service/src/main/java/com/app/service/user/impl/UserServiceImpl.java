package com.app.service.user.impl;

import com.app.data.repository.user.UserRepository;
import com.app.model.dto.user.CreateUserDto;
import com.app.model.dto.user.GetUserDto;
import com.app.service.email.EmailService;
import com.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final Environment environment;
    @Value("${userActivation.email.subject}")
    private String activationMailSubject;
    @Value("${userActivation.email.content}")
    private String activationMailContent;
    private static final Logger logger = LogManager.getLogger("DebugLogger");

    @Override
    public GetUserDto register(CreateUserDto createUserDto) {
        if(!validateCreateUserDto(createUserDto)){
            throw new IllegalStateException("Wrong inputted data. Impossible to create new user.");
        }
        var activationTimeMs = environment.getRequiredProperty("userActivation.activationTimeMs",Long.class);

        var username = createUserDto.username();
        var email = createUserDto.email();

        if(userRepository.findByUserName(username).isPresent()){
            throw new IllegalStateException("Username already exist.");
        }

        if(userRepository.findByEmail(email).isPresent()){
            throw new IllegalStateException("Email already exist.");
        }

        var password =  createUserDto.password();
        var userToRegister = createUserDto
                .toUser()
                .withPassword(passwordEncoder.encode(password));

        var insertedUser =  userRepository.save(userToRegister);

        var timestamp = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()+activationTimeMs;

        emailService.send(
                insertedUser.getEmail(),
                activationMailSubject,
                activationMailContent + " http://localhost:8080/users/activate?id="
                        + insertedUser.getId()
                        + "&timestamp="
                        + timestamp
        );
        return insertedUser.toGetUserDto();
    }

    @Override
    public GetUserDto activate(Long userId, Long expirationTime) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is null");
        }
        if (expirationTime == null) {
            throw new IllegalArgumentException("Expiration time is null");
        }

        var currentTime = LocalDateTime
                .now()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();
        if (expirationTime < currentTime) {
            throw new IllegalStateException("Activation link expired");
        }

        return userRepository
                .findById(userId)
                .map(user -> userRepository
                        .update(userId, user.withEnabled(true))
                        .toGetUserDto()
                )
                .orElseThrow();
    }
    
    private boolean validateCreateUserDto(CreateUserDto createUserDto){

        String emailRegex = environment.getRequiredProperty("validator.emailRegex");

        if(createUserDto.username() == null || createUserDto.password() == null || createUserDto.passwordConfirmation() == null || createUserDto.email() == null || createUserDto.role() == null){
            logger.error("At least one field in createUserDto is null.");
            return false;
        }

        if(!createUserDto.password().equals(createUserDto.passwordConfirmation())){
            logger.error("Password and password confirmation are different.");
            return false;
        }
        return createUserDto.email().matches(emailRegex);
    }
}
