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
        /*User user = userStorage.getUserById(id);
        User friendUser = getUserById(friendId);
        if (user.getUserFriends() == null) {
            user.setUserFriends(new HashSet<>());
        }
        if (friendUser.getUserFriends() == null) {
            friendUser.setUserFriends(new HashSet<>());
        }
        if (user.getUserFriends().contains(friendUser.getId()) || friendUser.getUserFriends().contains(user.getId())) {
            log.error("Пользователи уже являются друзьями.");
            throw new ValidationException("Пользователи уже являются друзьями.");
        }
        Set<Integer> friendsUser = user.getUserFriends();
        Set<Integer> friendsUserFriend = friendUser.getUserFriends();
        friendsUser.add(friendId);
        friendsUserFriend.add(id);
        user.setUserFriends(friendsUser);
        friendUser.setUserFriends(friendsUserFriend);*/
        log.debug("Пользователи добавлены друг к другу в друзья");
    }

    public void deleteUserFromFriends(Integer id, Integer friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        friendStorage.deleteUserFromFriends(id, friendId);
        log.info("Пользователь успешно удален из списка друзей");
        /*User user = getUserById(id);
        log.debug("Пользователь с id {} существует", id);
        User friendUser = getUserById(friendId);
        log.debug("Друг пользователя с id {} существует", friendId);
        if (user.getUserFriends() == null) {
            log.error("У пользователя {} нет друзей", id);
            user.setUserFriends(new HashSet<>());
        }
        if (friendUser.getUserFriends() == null) {
            log.error("У друга пользователя {} нет друзей", friendId);
            friendUser.setUserFriends(new HashSet<>());
        }
        if ((user.getUserFriends().contains(friendId)) && (friendUser.getUserFriends().contains(id))) {
            user.getUserFriends().remove(friendId);
            log.debug("Удалили из списка друзей пользователя с id {} друга с id {}", id, friendId);
            friendUser.getUserFriends().remove(id);
            log.debug("Удалили из списка друзей друга пользователя с id {} пользователя с id {}", friendId, id);
            log.info("Пользователи успешно удалены из списков друзей");
        } else {
            log.error("Пользователи не являются друзьями");
        }*/
    }

    public List<User> getFriendsByUser(Integer id) {
        userStorage.getUserById(id);
        return userStorage.getFriendsByUser(id);
        /*User user = getUserById(id);
        Set<Integer> userFriends = user.getUserFriends();
        if (userFriends == null) {
            userFriends = new HashSet<>();
            user.setUserFriends(userFriends);
        }
        return user.getUserFriends().stream()
                .map(friendId -> getUserById(friendId))
                .collect(Collectors.toList());*/
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        userStorage.getUserById(id);
        userStorage.getUserById(otherId);
        return userStorage.getCommonFriends(id, otherId);
        /*HashSet<Integer> userFriends = (HashSet<Integer>) getUserById(id).getUserFriends();
        if (userFriends == null || userFriends.isEmpty()) {
            log.error("У пользователя нет друзей.");
            throw new ValidationException("У пользователя нет друзей");
        }
        HashSet<Integer> otherUserFriends = (HashSet<Integer>) getUserById(otherId).getUserFriends();
        if (otherUserFriends == null || otherUserFriends.isEmpty()) {
            log.error("У пользователя нет друзей");
            throw new ValidationException("У пользователя нет друзей");
        }
        HashSet<Integer> commonFriends = new HashSet<>(userFriends);
        commonFriends.retainAll(otherUserFriends);
        return commonFriends.stream()
                .map(friendId -> getUserById(friendId))
                .collect(Collectors.toList());*/
    }
}
