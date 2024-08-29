package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
    }

    @Test
    void whenUserFieldsAreCorrectThenNotValidationException() {
        User user = User.builder()
                .email("user@email")
                .login("user")
                .name("user_name")
                .birthday(LocalDate.of(1997, 8, 14))
                .build();
        assertDoesNotThrow(() -> userController.validate(user));
    }

    @Test
    void whenUserEmailIsEmptyThenThrowValidationexception() {
        User user = User.builder()
                .email("")
                .login("user")
                .name("user_name")
                .birthday(LocalDate.of(1997, 8, 14))
                .build();
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void whenUserEmailNotContainSymbolThenThrowValidationexception() {
        User user = User.builder()
                .email("email")
                .login("user")
                .name("user_name")
                .birthday(LocalDate.of(1997, 8, 14))
                .build();
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void whenUserLoginIsEmptyThenThrowValidationexception() {
        User user = User.builder()
                .email("user@email")
                .login("")
                .name("user_name")
                .birthday(LocalDate.of(1997, 8, 14))
                .build();
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void whenUserLoginContainWhitespaceThenThrowValidationexception() {
        User user = User.builder()
                .email("user@email")
                .login("user login")
                .name("user_name")
                .birthday(LocalDate.of(1997, 8, 14))
                .build();
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void whenUserBirthdayInFutureThenThrowValidationexception() {
        User user = User.builder()
                .email("user@email")
                .login("user")
                .name("user_name")
                .birthday(LocalDate.of(2025, 8, 30))
                .build();
        assertThrows(ValidationException.class, () -> userController.validate(user));
    }

    @Test
    void whenUserNameIsEmptyThenNameEqualLogin() {
        User user = User.builder()
                .email("user@email")
                .login("user")
                .name("")
                .birthday(LocalDate.of(1997, 8, 14))
                .build();
        assertDoesNotThrow(() -> userController.validate(user));
        assertEquals(user.getLogin(), user.getName(), "Логин и имя не равны");
    }
}
