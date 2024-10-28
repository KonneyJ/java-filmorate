package ru.yandex.practicum.filmorate.storage.film_genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {
    void addGenreToFilm(Integer filmId, Integer genreId);

    List<Genre> getAllGenresByFilm(Integer id);
}
