package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User getUserById(Integer id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            log.error("Пользователь не найден!");
            throw new NotFoundException("Пользователь не найден!");
        } else {
            return user;
        }
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        userStorage.getUserById(user.getId());
        User updatedUser = userStorage.updateUser(user);
        if (updatedUser == null) {
            throw new NotFoundException("Такого пользователя нет, обновление невозможно!");
        } else {
            return updatedUser;
        }
    }

    public void addUserToFriends(Integer id, Integer friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        friendStorage.addUserToFriends(id, friendId);
        log.debug("Пользователи добавлены друг к другу в друзья");
    }

    public void deleteUserFromFriends(Integer id, Integer friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        friendStorage.deleteUserFromFriends(id, friendId);
        log.info("Пользователь успешно удален из списка друзей");
    }

    public List<User> getFriendsByUser(Integer id) {
        userStorage.getUserById(id);
        return userStorage.getFriendsByUser(id);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        userStorage.getUserById(id);
        userStorage.getUserById(otherId);
        return userStorage.getCommonFriends(id, otherId);
    }
}
