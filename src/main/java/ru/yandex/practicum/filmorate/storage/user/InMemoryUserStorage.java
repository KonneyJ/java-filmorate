package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class InMemoryUserStorage {
    private int nextId = 1;
    private final Map<Integer, User> userStorage = new HashMap<>();

    public User getUserById(Integer id) {
        if (userStorage.containsKey(id)) {
            return userStorage.get(id);
        } else {
            return null;
        }
    }

    public List<User> getUsers() {
        log.debug("GET /users");
        return new ArrayList<>(userStorage.values());
    }

    public User createUser(User user) {
        log.debug("POST /users with {}", user);
        user.setId(getNextId());
        userStorage.put(user.getId(), user);
        return user;
    }


    public User updateUser(User user) {
        log.debug("PUT /users with {}", user);
        if (!userStorage.containsKey(user.getId())) {
            return null;
        }
        User userFromStorage = userStorage.get(user.getId());
        userFromStorage.setId(user.getId());
        userFromStorage.setEmail(user.getEmail());
        userFromStorage.setLogin(user.getLogin());
        userFromStorage.setName(user.getName());
        userFromStorage.setBirthday(user.getBirthday());
        userStorage.put(user.getId(), userFromStorage);
        return userFromStorage;
    }

    private int getNextId() {
        return nextId++;
    }
}
