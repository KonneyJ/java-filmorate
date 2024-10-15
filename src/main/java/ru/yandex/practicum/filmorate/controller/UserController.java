package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        log.debug("GET /users by id {}", id);
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getUsers() {
        log.debug("GET /users");
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.debug("POST /users with {}", user);
        validate(user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("PUT /users with {}", user);
        validate(user);
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addUserToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("PUT /users/{id}/friends by friendId {}", friendId);
        userService.addUserToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteUserFromFriends(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        log.debug("DELETE /users/{id}/friends by friendId {}", friendId);
        userService.deleteUserFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsByUser(@PathVariable Integer id) {
        log.debug("GET /users/{id}/friends by id {}", id);
        return userService.getFriendsByUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.debug("GET /users/{id}/friends/common by id {} and otherId {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
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
}
