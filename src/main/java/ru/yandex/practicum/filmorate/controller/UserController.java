package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private int nextId = 1;
    private final Map<Integer, User> userStorage = new HashMap<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        log.debug("GET /users");
        return new ArrayList<>(userStorage.values());
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        log.debug("POST /users with {}", user);
        validate(user);
        user.setId(getNextId());
        userStorage.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.debug("PUT /users with {}", user);
        if (!userStorage.containsKey(user.getId())) {
            throw new ValidationException("Такого пользователя нет, обновление невозможно!");
        }
        validate(user);
        User userFromStorage = userStorage.get(user.getId());
        userFromStorage.setId(user.getId());
        userFromStorage.setEmail(user.getEmail());
        userFromStorage.setLogin(user.getLogin());
        userFromStorage.setName(user.getName());
        userFromStorage.setBirthday(user.getBirthday());
        userStorage.put(user.getId(), userFromStorage);
        return userFromStorage;
    }

    void validate(User user) {
        log.debug("Validation start for {}", user);

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String message = "Email пользователя не может быть пустым и должен содержать символ @! email = "
                    + user.getEmail();
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String message = "Логин не может быть пустым и содержать пробелы!";
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String message = "Дата рождения не может быть в будущем!";
            log.error(message);
            throw new ValidationException(message);
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private int getNextId() {
        return nextId++;
    }
}
