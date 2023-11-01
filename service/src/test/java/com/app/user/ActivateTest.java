package com.app.user;


import com.app.config.AppTestBeansConfig;
import com.app.data.repository.user.UserRepository;
import com.app.model.user.Role;
import com.app.model.user.User;
import com.app.service.email.EmailService;
import com.app.service.user.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource(locations = "classpath:application_test.properties")
public class ActivateTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    EmailService emailService;
    @Mock
    Environment environment;

    @InjectMocks
    UserServiceImpl userService;


    @Test
    @DisplayName("When activate method is called and inputted user id is null")
    void test1() {
        Assertions.assertThatThrownBy(() -> userService.activate(null, LocalDateTime
                        .now()
                        .toInstant(ZoneOffset.UTC)
                        .toEpochMilli()- 1000000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User id is null");
    }

    @Test
    @DisplayName("When activate method is called and inputted expiration time is null")
    void test2() {
        Assertions.assertThatThrownBy(() -> userService.activate(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Expiration time is null");
    }

    @Test
    @DisplayName("When activate method is called and inputted expiration time has expired")
    void test3() {
        Assertions.assertThatThrownBy(() -> userService.activate(1L, LocalDateTime
                        .now()
                        .toInstant(ZoneOffset.UTC)
                        .toEpochMilli() - 1000000L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Activation link expired");
    }

    @Test
    @DisplayName("When activate method is called with correct arguments and it should generate specified output")
    void test4() {

        var user = User.builder()
                .id(1L)
                .username("AAA")
                .password("AAA")
                .email("AAA")
                .role(Role.USER)
                .enabled(false)
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.update(1L, user.withEnabled(true)))
                .thenReturn(user.withEnabled(true));

        Assertions.assertThat(userService.activate(1L, LocalDateTime
                        .now()
                        .toInstant(ZoneOffset.UTC)
                        .toEpochMilli() + 1000000L))
                .isEqualTo(user.toGetUserDto());
    }
}
