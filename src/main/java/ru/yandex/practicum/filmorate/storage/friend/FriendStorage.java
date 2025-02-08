package ru.yandex.practicum.filmorate.storage.friend;

public interface FriendStorage {
    void addUserToFriends(Integer userId, Integer friendId);

    void deleteUserFromFriends(Integer userId, Integer friendId);

}
