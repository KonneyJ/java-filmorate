package ru.yandex.practicum.filmorate.storage.friend;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@AllArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbc;

    @Override
    public void addUserToFriends(Integer userId, Integer friendId) {
        try {
            String query = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
            jdbc.update(query, userId, friendId);
        } catch (Exception e) {
            log.error("Произошла ошибка при добавлении пользователя в список друзей");
        }
    }

    @Override
    public void deleteUserFromFriends(Integer userId, Integer friendId) {
        try {
            String query = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
            jdbc.update(query, userId, friendId);
        } catch (Exception e) {
            log.error("Произошла ошибка при удалении пользователя из списка друзей");
        }
    }
}
