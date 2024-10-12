package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public String putLikeToFilm(Integer id, Integer userId) {
        Film film = getFilmById(id);
        if (film.getFilmLikes() == null) {
            film.setFilmLikes(new HashSet<>());
        }
        if (film.getFilmLikes().contains(userId)) {
            log.error("Пользователь уже поставил лайк этому фильму");
            throw new ValidationException("Пользователь уже поставил лайк этому фильму");
        }
        Set<Integer> filmLikes = film.getFilmLikes();
        filmLikes.add(userId);
        film.setFilmLikes(filmLikes);
        return "Фильм успешно пролайкан";
    }

    public String deleteLikeFromFilm(Integer id, Integer userId) {
        Film film = getFilmById(id);
        if (film.getFilmLikes() == null || film.getFilmLikes().isEmpty()) {
            log.error("У фильма нет лайков");
            throw new NotFoundException("У фильма нет лайков");
        }
        if (film.getFilmLikes().contains(userId)) {
            film.getFilmLikes().remove(userId);
            log.debug("Лайк успешно удален");
            return "Лайк успешно удален";
        } else {
            log.error("Данный пользователь не ставил лайк этому фильму");
            throw new NotFoundException("Данный пользователь не ставил лайк этому фильму");
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getFilms().stream()
                .filter(film -> film.getFilmLikes() != null)
                .filter(film -> !film.getFilmLikes().isEmpty())
                .sorted((film1, film2) -> film2.getFilmLikes().size() - film1.getFilmLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
