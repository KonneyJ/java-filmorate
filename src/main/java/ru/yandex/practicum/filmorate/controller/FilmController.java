package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("PUT /films with {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public String putLikeToFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug("PUT /films/{id}/like/{userId} by id {} and userId {}", id, userId);
        return filmService.putLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String deleteLikeFromFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.debug("DELETE /films/{id}/like/{userId} by id {} and userId {}", id, userId);
        return filmService.deleteLikeFromFilm(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false, defaultValue = "10") String count) {
        log.debug("GET /films/popular");
        return filmService.getPopularFilms(Integer.parseInt(count));
    }
}
