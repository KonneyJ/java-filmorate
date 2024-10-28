package ru.yandex.practicum.filmorate.storage.film_genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataUpdateException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbc;
    private final GenreMapper genreMapper;

    @Override
    public void addGenreToFilm(Integer filmId, Integer genreId) {
        try {
            String query = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
            jdbc.update(query, filmId, genreId);
        } catch (DataAccessException e) {
            throw new DataUpdateException("Не удалось обновить данные");
        }
    }

    @Override
    public List<Genre> getAllGenresByFilm(Integer id) {
        try {
            String query = "SELECT * FROM genre WHERE genre_id IN (SELECT genre_id FROM film_genre WHERE film_id = ?)";
            return jdbc.query(query, genreMapper, id);
        } catch (DataAccessException e) {
            throw new DataUpdateException("Не удалось получить данные");
        }
    }
}
