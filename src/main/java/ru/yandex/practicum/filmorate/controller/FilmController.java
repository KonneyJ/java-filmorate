package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) {
        log.debug("GET /films by id {}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.debug("GET /films");
        return filmService.getFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.debug("POST /films with {}", film);
        validate(film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("PUT /films with {}", film);
        validate(film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLikeToFilm(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        log.debug("PUT /films/{id}/like/{userId} by id {} and userId {}", filmId, userId);
        filmService.putLikeToFilm(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.debug("DELETE /films/{id}/like/{userId} by id {} and userId {}", id, userId);
        filmService.deleteLikeFromFilm(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") String count) {
        log.debug("GET /films/popular");
        return filmService.getPopularFilms(Integer.parseInt(count));
    }

    void validate(Film film) {
        log.debug("Validation start for {}", film);

        final LocalDate DATE_OF_FIRST_FILM = LocalDate.of(1895, 12, 28);

        if (film.getName() == null || film.getName().isEmpty()) {
            String message = "Имя фильма не может быть пустым! name = " + film.getName();
            log.error(message);
            throw new ValidationException(message);
        }
        if (film.getDescription().length() > 200) {
            String message = "Максимальная длина описания - 200 символов! length = " + film.getDescription().length();
            log.error(message);
            throw new ValidationException(message);
        }
        if (film.getReleaseDate().isBefore(DATE_OF_FIRST_FILM)) {
            String message = "Дата релиза не может быть раньше 28 декабря 1895 года! date = " + film.getReleaseDate();
            log.error(message);
            throw new ValidationException(message);
        }
        if (film.getDuration() < 0) {
            String message = "Продолжительность фильма не может быть меньше нуля! duration = " + film.getDuration();
            log.error(message);
            throw new ValidationException(message);
        }
    }
}
