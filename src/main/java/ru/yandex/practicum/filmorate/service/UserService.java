package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createuser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public String addUserToFriends(Integer id, Integer friendId) {
        User user = userStorage.getUserById(id);
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
        friendUser.setUserFriends(friendsUserFriend);
        log.debug("Пользователи добавлены друг к другу в друзья");
        return "Пользователи добавлены друг к другу в друзья";
    }

    public String deleteUserFromFriends(Integer id, Integer friendId) {
        User user = getUserById(id);
        User friendUser = getUserById(friendId);
        if (user.getUserFriends() == null) {
            log.error("У пользователя нет друзей");
            throw new NotFoundException("У пользователя нет друзей");
        }
        if (friendUser.getUserFriends() == null) {
            log.error("У пользователя нет друзей");
            throw new NotFoundException("У пользователя нет друзей");
        }
        if (user.getUserFriends().contains(friendUser.getId()) || friendUser.getUserFriends().contains(user.getId())) {
            user.getUserFriends().remove(friendId);
            friendUser.getUserFriends().remove(id);
            log.debug("Пользователи успешно удалены из списков друзей");
            return "Пользователь успешно удален из списка друзей";
        } else {
            log.error("Пользователи не являются друзьями");
            throw new NotFoundException("Пользователи не являются друзьями");
        }
    }

    public List<User> getFriendsByUser(Integer id) {
        User user = getUserById(id);
        Set<Integer> userFriends = user.getUserFriends();
        if (userFriends == null) {
            userFriends = new HashSet<>();
            user.setUserFriends(userFriends);
        }
        return user.getUserFriends().stream()
                .map(friendId -> getUserById(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        HashSet<Integer> userFriends = (HashSet<Integer>) getUserById(id).getUserFriends();
        if (userFriends == null || userFriends.isEmpty()) {
            log.error("У пользователя нет друзей");
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
                .collect(Collectors.toList());
    }
}
