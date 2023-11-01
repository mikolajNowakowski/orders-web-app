package com.app.user;

import com.app.config.AppTestBeansConfig;
import com.app.data.repository.user.UserRepository;
import com.app.model.dto.user.CreateUserDto;
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

import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = AppTestBeansConfig.class)
@TestPropertySource(locations = "classpath:application_test.properties")
public class RegisterTest {
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
    @DisplayName("When register method input data is incorrect due to at least one null argument.")
    void test1() {
        Assertions.assertThatThrownBy(() -> userService.register(new CreateUserDto(null, "AAA", "AAA", "123abcd@gmail.com", Role.USER)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Wrong inputted data. Impossible to create new user.");
    }

    @Test
    @DisplayName("When register method input data is incorrect due to differences between password and password confirmation.")
    void test2() {
        Assertions.assertThatThrownBy(() -> userService.register(new CreateUserDto("AAA", "AAA", "BBB", "123abcd@gmail.com", Role.USER)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Wrong inputted data. Impossible to create new user.");
    }

    @Test
    @DisplayName("When register method input data is incorrect due to wrong mail form.")
    void test3() {

        when(environment.getRequiredProperty("validator.emailRegex"))
                .thenReturn("^[a-zA-Z0-9._%+-]+@(gmail|o2|onet)\\.(pl|com)$");
        Assertions.assertThatThrownBy(() -> userService.register(new CreateUserDto("AAA", "AAA", "AAA", "123123123XYZ", Role.USER)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Wrong inputted data. Impossible to create new user.");
    }

    @Test
    @DisplayName("When register method is called with inputted name already exist.")
    void test4() {

        when(environment.getRequiredProperty("validator.emailRegex"))
                .thenReturn("^[a-zA-Z0-9._%+-]+@(gmail|o2|onet)\\.(pl|com)$");
        when(userRepository.findByUserName("AAA"))
                .thenReturn(Optional.of(User.builder().build()));

        Assertions.assertThatThrownBy(() -> userService.register(new CreateUserDto("AAA", "AAA", "AAA", "123abcd@gmail.com", Role.USER)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Username already exist.");
    }


    @Test
    @DisplayName("When register method is called and user with inputted name already exist.")
    void test5() {

        when(environment.getRequiredProperty("validator.emailRegex"))
                .thenReturn("^[a-zA-Z0-9._%+-]+@(gmail|o2|onet)\\.(pl|com)$");
        when(userRepository.findByEmail("123abcd@gmail.com"))
                .thenReturn(Optional.of(User.builder().build()));

        Assertions.assertThatThrownBy(() -> userService.register(new CreateUserDto("AAA", "AAA", "AAA", "123abcd@gmail.com", Role.USER)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Email already exist.");
    }

    @Test
    @DisplayName("When register method was called with correct arguments")
    void test6() {

        when(environment.getRequiredProperty("validator.emailRegex"))
                .thenReturn("^[a-zA-Z0-9._%+-]+@(gmail|o2|onet)\\.(pl|com)$");
        when(environment.getRequiredProperty("userActivation.activationTimeMs",Long.class))
                .thenReturn(120000L);


        when(passwordEncoder.encode("AAA"))
                .thenReturn("AAA");

        when(userRepository.save(new CreateUserDto("AAA", "AAA", "AAA", "123abcd@gmail.com", Role.USER).toUser().withPassword("AAA")))
                .thenReturn(User.builder()
                        .id(1L)
                        .username("AAA")
                        .password("AAA")
                        .email("123abcd@gmail.com")
                        .role(Role.USER)
                        .enabled(false)
                        .build());

        Assertions.assertThat(userService.register(new CreateUserDto("AAA", "AAA", "AAA", "123abcd@gmail.com", Role.USER)))
                .isEqualTo(User.builder()
                        .id(1L)
                        .username("AAA")
                        .password("AAA")
                        .email("123abcd@gmail.com")
                        .role(Role.USER)
                        .enabled(false)
                        .build().toGetUserDto());
    }
}
