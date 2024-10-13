package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
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

    public void putLikeToFilm(Film film, User user) {
        if (film.getFilmLikes() == null) {
            film.setFilmLikes(new HashSet<>());
        }
        if (film.getFilmLikes().contains(user.getId())) {
            log.error("Пользователь уже поставил лайк этому фильму");
            throw new ValidationException("Пользователь уже поставил лайк этому фильму");
        }
        film.getFilmLikes().add(user.getId());
        log.debug("Фильму успешно добавлен лайк");
    }

    public void deleteLikeFromFilm(Integer id, Integer userId) {
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

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getFilms().stream()
                .filter(film -> film.getFilmLikes() != null)
                .filter(film -> !film.getFilmLikes().isEmpty())
                .sorted((film1, film2) -> film2.getFilmLikes().size() - film1.getFilmLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
