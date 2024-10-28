package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataUpdateException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.sql.PreparedStatement;
import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbc;
    private final FilmMapper filmMapper;
    private final FilmGenreStorage filmGenreStorage;
    private final LikeStorage likeStorage;

    @Override
    public Film getFilmById(Integer id) {
        String query = "SELECT f.*, m.name AS mpa_name FROM films AS f LEFT JOIN mpa AS m ON m.mpa_id=f.mpa_id WHERE id = ?";
        Film film = jdbc.queryForObject(query, filmMapper, id);
        List<Genre> genresByFilm = filmGenreStorage.getAllGenresByFilm(id);
        /*if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> filmGenreStorage.addGenreToFilm(film.getId(), genre.getId()));
        }*/
        film.setGenres(genresByFilm);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String query = "SELECT f.*, m.name AS mpa_name FROM films AS f LEFT JOIN mpa AS m ON m.mpa_id=f.mpa_id";
        List<Film> films = jdbc.query(query, filmMapper);

        /*films.forEach(film -> {
            Integer id = film.getId();
            film.setFilmLikes((Set<Like>) likeStorage.getLikesByFilm(id));
        });*/
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        try {
            String query = "INSERT INTO films (name, description, release_date, duration, mpa_id)\n" +
                    "VALUES ( ?, ?, ?, ?, ?)";
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbc.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement(query, new String[]{"id"});
                ps.setString(1, film.getName());
                ps.setString(2, film.getDescription());
                ps.setObject(3, film.getReleaseDate());
                ps.setInt(4, film.getDuration());
                ps.setInt(5, film.getMpa().getId());
                return ps;
            }, keyHolder);

            Integer id = keyHolder.getKeyAs(Integer.class);

            // Возвращаем id нового пользователя
            if (id != null) {
                film.setId(id);
            } else {
                throw new InternalServerException("Не удалось сохранить данные");
            }

            if (film.getGenres() != null) {
                film.getGenres().forEach(genre -> filmGenreStorage.addGenreToFilm(film.getId(), genre.getId()));
            }
            //Collection<Like> filmLikes = likeStorage.getLikesByFilm(id);
            //film.setFilmLikes((Set<Like>) filmLikes);
            return film;
        } catch (DataAccessException e) {
            throw new DataUpdateException("Не удалось сохранить данные");

        }
    }

    @Override
    public Film updateFilm(Film film) {
        String query = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                "WHERE id = ?";
        int rowsUpdated = jdbc.update(query, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (rowsUpdated == 0) {
            throw new InternalServerException("Не удалось обновить данные");
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String query = "SELECT f.*, m.name AS mpa_name FROM films AS f LEFT JOIN mpa AS m ON m.mpa_id=f.mpa_id WHERE f.id IN (SELECT film_id FROM film_likes GROUP BY film_id " +
                "ORDER BY COUNT(film_id) DESC )";
        if (count > getFilms().size()) {
            return jdbc.query(query, filmMapper);
        }
        return jdbc.query(query.concat("LIMIT ?"), filmMapper, count);
    }

}
