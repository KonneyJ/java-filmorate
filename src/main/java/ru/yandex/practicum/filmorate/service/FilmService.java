package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikeStorage likeStorage;

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
        likeStorage.putLikeToFilm(filmId, userId);
        /*userService.getUserById(userId);
        Film film = getFilmById(filmId);
        if (film.getFilmLikes() == null) {
            film.setFilmLikes(new HashSet<>());
        }
        if (film.getFilmLikes().contains(userId)) {
            log.error("Пользователь уже поставил лайк этому фильму");
            throw new ValidationException("Пользователь уже поставил лайк этому фильму");
        }
        film.getFilmLikes().add(userId);*/
        log.debug("Фильму успешно поставлен лайк");
    }

    public void deleteLikeFromFilm(Integer id, Integer userId) {
        likeStorage.deleteLikeFromFilm(id, userId);
        /*userService.getUserById(userId);
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
        film.getFilmLikes().remove(userId);*/
        log.debug("Лайк успешно удален");
    }

    public Collection<Film> getPopularFilms(Integer count) {
        if (count < 1) {
            throw new ValidationException("Количество фильмов должно быть больше 0");
        }
        return filmStorage.getPopularFilms(count);
        /*return filmStorage.getFilms().stream()
                .filter(film -> film.getFilmLikes() != null)
                .filter(film -> film.getFilmLikes().size() > 0)
                .sorted((film1, film2) -> film2.getFilmLikes().size() - film1.getFilmLikes().size())
                .limit(count)
                .collect(Collectors.toList());*/
    }
}
