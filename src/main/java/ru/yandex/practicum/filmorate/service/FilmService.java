package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            log.error("Фильм не найден!");
            throw new NotFoundException("Фильм не найден!");
        } else {
            return film;
        }
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        Film updatedFilm = filmStorage.updateFilm(film);
        if (updatedFilm == null) {
            throw new NotFoundException("Такого фильма нет, обновление невозможно!");
        } else {
            return updatedFilm;
        }
    }

    public void putLikeToFilm(Integer filmId, Integer userId) {
        userService.getUserById(userId);
        Film film = getFilmById(filmId);
        if (film.getFilmLikes() == null) {
            film.setFilmLikes(new HashSet<>());
        }
        if (film.getFilmLikes().contains(userId)) {
            log.error("Пользователь уже поставил лайк этому фильму");
            throw new ValidationException("Пользователь уже поставил лайк этому фильму");
        }
        film.getFilmLikes().add(userId);
        log.debug("Фильму успешно добавлен лайк");
    }

    public void deleteLikeFromFilm(Integer id, Integer userId) {
        userService.getUserById(userId);
        Film film = getFilmById(id);
        if (film.getFilmLikes() == null) {
            film.setFilmLikes(new HashSet<>());
            log.error("У фильма нет лайков");
            throw new NotFoundException("У фильма нет лайков");
        }
        if (!film.getFilmLikes().contains(userId)) {
            log.error("Данный пользователь не ставил лайк этому фильму");
            throw new NotFoundException("Данный пользователь не ставил лайк этому фильму");
        }
        film.getFilmLikes().remove(userId);
        log.debug("Лайк успешно удален");
    }

    public Collection<Film> getPopularFilms(Integer count) {
        return filmStorage.getFilms().stream()
                .filter(film -> film.getFilmLikes() != null)
                .filter(film -> !film.getFilmLikes().isEmpty())
                .sorted((film1, film2) -> film2.getFilmLikes().size() - film1.getFilmLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
