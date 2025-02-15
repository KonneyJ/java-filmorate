package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User getUserById(Integer id);

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    List<User> getFriendsByUser(Integer id);

    List<User> getCommonFriends(Integer id, Integer otherId);
}
