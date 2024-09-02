package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    void whenFilmFieldsAreCorrectThenNotValidationException() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1997, 8, 14))
                .duration(140)
                .build();
        assertDoesNotThrow(() -> filmController.validate(film));
    }

    @Test
    void whenFilmNameIsEmptyThenThrowValidationException() {
        Film film = Film.builder()
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(1997, 8, 14))
                .duration(140)
                .build();
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void whenFilmDescription201SymbolsThenThrowValidationException() {
        Film film = Film.builder()
                .name("Film")
                .description("Рандомное описание фильма, которое должно быть больше, чем 200 символов. Нужно " +
                        "написать что-нибудь еще, чтобы набрать 200 символов и проверить валидацию фильма. " +
                        "Надеюсь тут уже есть двести символов!!!!")
                .releaseDate(LocalDate.of(1997, 8, 14))
                .duration(140)
                .build();
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void whenFilmDescriptionIs200SymbolsThenNotThrowValidationException() {
        Film film = Film.builder()
                .name("Film")
                .description("Рандомное описание фильма, которое должно быть больше, чем 200 символов. Нужно " +
                        "написать что-нибудь еще, чтобы набрать 200 символов и проверить валидацию фильма. " +
                        "Надеюсь тут уже есть двести символов!!!")
                .releaseDate(LocalDate.of(1997, 8, 14))
                .duration(140)
                .build();
        assertDoesNotThrow(() -> filmController.validate(film));
    }

    @Test
    void whenFilmDateReleaseIsNotCorrectThenThrowValidationException() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(140)
                .build();
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void whenFilmDateReleaseIsCorrectThenNotThrowValidationException() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(140)
                .build();
        assertDoesNotThrow(() -> filmController.validate(film));
    }

    @Test
    void whenFilmDurationIsNotCorrectThenThrowValidationException() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1997, 8, 14))
                .duration(-1)
                .build();
        assertThrows(ValidationException.class, () -> filmController.validate(film));
    }

    @Test
    void whenFilmDurationIsCorrectThenNotThrowValidationException() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(1997, 8, 14))
                .duration(0)
                .build();
        assertDoesNotThrow(() -> filmController.validate(film));
    }
}
