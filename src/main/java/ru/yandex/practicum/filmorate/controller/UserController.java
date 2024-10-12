package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

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
        return userService.createuser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("PUT /users with {}", user);
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addUserToFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("PUT /users/{id}/friends by friendId {}", friendId);
        return userService.addUserToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteUserFromFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("DELETE /users/{id}/friends by friendId {}", friendId);
        return userService.deleteUserFromFriends(id, friendId);
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
}
