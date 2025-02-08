package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbc;
    private final GenreMapper genreMapper;

    @Override
    public List<Genre> getAllGenres() {
        String query = "SELECT * FROM genre ORDER BY genre_id";
        return jdbc.query(query, genreMapper);
    }

    @Override
    public Genre getGenreById(Integer id) {
        try {
            String query = "SELECT * FROM genre WHERE genre_id = ?";
            return jdbc.queryForObject(query, genreMapper, id);
        } catch (Exception e) {
            throw new NotFoundException("Такой жанр не найден");
        }
    }
}
