package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int nextId = 1;
    private final Map<Integer, Film> filmStorage = new HashMap<>();

    @Override
    public Film getFilmById(Integer id) {
        return filmStorage.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("POST /films with {}", film);
        validate(film);
        film.setId(getNextId());
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("PUT /films with {}", film);
        if (!filmStorage.containsKey(film.getId())) {
            throw new ValidationException("Такого фильма нет, обновление невозможно!");
        }
        validate(film);
        Film filmFromStorage = filmStorage.get(film.getId());
        filmFromStorage.setId(film.getId());
        filmFromStorage.setName(film.getName());
        filmFromStorage.setDescription(film.getDescription());
        filmFromStorage.setDuration(film.getDuration());
        filmFromStorage.setReleaseDate(film.getReleaseDate());
        filmStorage.put(film.getId(), filmFromStorage);
        return filmFromStorage;
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

    private int getNextId() {
        return nextId++;
    }
}
