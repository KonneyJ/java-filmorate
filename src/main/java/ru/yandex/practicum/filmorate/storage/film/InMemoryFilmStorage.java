package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

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
        if (filmStorage.containsKey(id)) {
            return filmStorage.get(id);
        } else {
            return null;
        }
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public Film createFilm(Film film) {
        log.debug("POST /films with {}", film);
        film.setId(getNextId());
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("PUT /films with {}", film);
        if (!filmStorage.containsKey(film.getId())) {
            return null;
        }
        Film filmFromStorage = filmStorage.get(film.getId());
        filmFromStorage.setId(film.getId());
        filmFromStorage.setName(film.getName());
        filmFromStorage.setDescription(film.getDescription());
        filmFromStorage.setDuration(film.getDuration());
        filmFromStorage.setReleaseDate(film.getReleaseDate());
        filmStorage.put(film.getId(), filmFromStorage);
        return filmFromStorage;
    }

    private int getNextId() {
        return nextId++;
    }
}
