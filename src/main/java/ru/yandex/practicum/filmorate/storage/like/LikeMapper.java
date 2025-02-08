package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LikeMapper implements RowMapper<Like> {

    @Override
    public Like mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Like like = new Like();
        like.setFilmId(resultSet.getInt("film_id"));
        like.setUserId(resultSet.getInt("user_id"));
        return like;
    }
}
