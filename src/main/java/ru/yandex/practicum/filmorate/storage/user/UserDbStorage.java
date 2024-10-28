package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataUpdateException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbc;
    private final UserMapper userMapper;

    @Override
    public User getUserById(Integer id) {
        try {
            String query = "SELECT * FROM users WHERE id = ?";
            return jdbc.queryForObject(query, userMapper, id);
        } catch (Exception e) {
            throw new NotFoundException("Такой пользователь не найден");
        }
    }

    @Override
    public List<User> getUsers() {
        String query = "SELECT * FROM users";
        return jdbc.query(query, userMapper);
    }

    @Override
    public User createUser(User user) {
        String query = "INSERT INTO users (email, login, name, birthday)\n" +
                "VALUES ( ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, new String[]{"id"});
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);

        // Возвращаем id нового пользователя
        if (id != null) {
            user.setId(id);
            return user;
        } else {
            throw new InternalServerException("Не удалось сохранить данные");
        }
    }

    @Override
    public User updateUser(User user) {
        try {
            String query = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
            int rowsUpdated = jdbc.update(query, user.getEmail(), user.getLogin(), user.getName(),
                    user.getBirthday(), user.getId());
            if (rowsUpdated == 0) {
                throw new NotFoundException("Такой пользователь не найден");
            }
            return user;
        } catch (DataAccessException e) {
            throw new DataUpdateException("Не удалось обновить данные");
        }
    }

    @Override
    public List<User> getFriendsByUser(Integer id) {
        try {
            String query = "SELECT * FROM users WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = ?)";
            return jdbc.query(query, userMapper, id);
        } catch (DataAccessException e) {
            log.debug("У пользователя нет друзей");
            return new ArrayList<>();
        }
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        try {
            String query = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ? )" +
                    "AND IN (SELECT friend_id FROM friends WHERE user_id = ?)";
            return jdbc.query(query, userMapper, id, otherId);
        } catch (DataAccessException e) {
            log.debug("У пользователей нет общих друзей");
            return null;
        }
    }
}
