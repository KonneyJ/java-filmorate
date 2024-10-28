package ru.yandex.practicum.filmorate.storage.like;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbc;
    private final LikeMapper likeMapper;

    @Override
    public void putLikeToFilm(Integer filmId, Integer userId) {
        String query = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbc.update(query, filmId, userId);
    }

    @Override
    public void deleteLikeFromFilm(Integer filmId, Integer userId) {
        String query = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbc.update(query, filmId, userId);
    }

    @Override
    public List<Like> getLikesByFilm(Integer id) {
        final String sql = "SELECT * FROM film_likes WHERE   film_id = ?";
        return jdbc.query(sql, likeMapper, id);
    }
}
