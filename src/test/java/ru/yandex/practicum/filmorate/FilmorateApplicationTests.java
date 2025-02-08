package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreDbStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeMapper;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaMapper;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {GenreStorage.class, GenreMapper.class, GenreService.class, GenreDbStorage.class,
        MpaStorage.class, MpaMapper.class, MpaService.class, MpaDbStorage.class, UserStorage.class, UserMapper.class,
        UserService.class, UserDbStorage.class, FilmStorage.class, FilmDbStorage.class, FilmService.class,
        FilmMapper.class, FriendDbStorage.class, FriendStorage.class, FilmGenreDbStorage.class, FilmGenreDbStorage.class,
        LikeDbStorage.class, LikeStorage.class, LikeMapper.class})
class FilmorateApplicationTests {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final LikeStorage likeStorage;
    private final FriendStorage friendStorage;

    private User user1;
    private User user2;
    private Film film1;
    private Film film2;
    private Mpa mpa1;
    private Mpa mpa2;
    private Genre genre1;
    private Genre genre2;

    @BeforeEach
    public void setUp() {
        mpa1 = new Mpa(1, "G");
        mpa2 = new Mpa(2, "PG");

        genre1 = new Genre(1, "Комедия");
        genre2 = new Genre(2, "Драма");

        user1 = new User();
        user1.setName("Julie");
        user1.setEmail("julie17@yandex.ru");
        user1.setLogin("julie17");
        user1.setBirthday(LocalDate.of(1998, 2, 14));

        user2 = new User();
        user2.setName("Dima");
        user2.setEmail("dima27@yandex.ru");
        user2.setLogin("dima27");
        user2.setBirthday(LocalDate.of(1988, 5, 6));

        film1 = new Film();
        film1.setName("Film1");
        film1.setDescription("Description by film 1");
        film1.setReleaseDate(LocalDate.of(1950, 5, 5));
        film1.setDuration(120);
        film1.setMpa(mpa1);
        film1.setGenres(List.of(genre1));

        film2 = new Film();
        film2.setName("Film2");
        film2.setDescription("Description by film 2");
        film2.setReleaseDate(LocalDate.of(1970, 5, 5));
        film2.setDuration(100);
        film2.setMpa(mpa2);
        film2.setGenres(List.of(genre1, genre2));
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldGetAllGenres() {
        List<Genre> genreList = genreStorage.getAllGenres();
        assertNotNull(genreList);
        assertEquals(6, genreList.size());
    }

    @Test
    void shouldGetGenreById() {
        Genre genre = genreStorage.getGenreById(1);
        assertEquals(genre1.getId(), genre.getId());
    }

    @Test
    void shouldGetAllMpa() {
        List<Mpa> mpaList = mpaStorage.getAllMpa();
        assertNotNull(mpaList);
        assertEquals(5, mpaList.size());
    }

    @Test
    void shouldGetMpaById() {
        Mpa mpa = mpaStorage.getMpaById(1);
        assertEquals(mpa1.getId(), mpa.getId());
    }

    @Test
    void shouldCreateFilm() {
        Film film = filmStorage.createFilm(film1);
        assertEquals(film1.getName(), film.getName());
        assertEquals(film1.getDescription(), film.getDescription());
        assertEquals(film1.getReleaseDate(), film.getReleaseDate());
        assertEquals(film1.getDuration(), film.getDuration());
    }

    @Test
    void shouldUpdateFilm() {
        filmStorage.createFilm(film1);
        film2.setId(film1.getId());
        Film film = filmStorage.updateFilm(film2);
        assertEquals(film2.getId(), film.getId());
        assertEquals(film2.getName(), film.getName());
        assertEquals(film2.getDescription(), film.getDescription());
        assertEquals(film2.getReleaseDate(), film.getReleaseDate());
        assertEquals(film2.getDuration(), film.getDuration());
    }

    @Test
    void shouldGetFilmById() {
        Film film = filmStorage.createFilm(film1);
        Integer filmId = film.getId();
        Film findedFilm = filmStorage.getFilmById(filmId);
        assertEquals(filmId, findedFilm.getId());
    }

    @Test
    void shouldGetAllFilms() {
        filmStorage.createFilm(film1);
        filmStorage.createFilm(film2);
        List<Film> newList = List.of(film1, film2);
        List<Film> filmList = filmStorage.getFilms();
        assertEquals(newList.size(), filmList.size());
    }

    @Test
    void shouldAddLikeToFilm() {
        Film film = filmStorage.createFilm(film1);
        User user = userStorage.createUser(user1);
        likeStorage.putLikeToFilm(film.getId(), user.getId());
        List<Like> likeList = likeStorage.getLikesByFilm(film.getId());
        assertNotNull(likeList);
        assertEquals(1, likeList.size());
    }

    @Test
    void shouldDeleteLikeFromFilm() {
        Film film = filmStorage.createFilm(film1);
        User user = userStorage.createUser(user1);
        likeStorage.putLikeToFilm(film.getId(), user.getId());
        List<Like> likeList = likeStorage.getLikesByFilm(film.getId());
        assertNotNull(likeList);
        assertEquals(1, likeList.size());

        likeStorage.deleteLikeFromFilm(film.getId(), user.getId());
        List<Like> likeList1 = likeStorage.getLikesByFilm(film.getId());
        assertNotNull(likeList1);
        assertEquals(0, likeList1.size());
    }

    @Test
    void shouldGetPopularFilms() {
        Film createdFilm1 = filmStorage.createFilm(film1);
        Film createdFilm2 = filmStorage.createFilm(film2);

        User createdUser1 = userStorage.createUser(user1);
        User createdUser2 = userStorage.createUser(user2);

        likeStorage.putLikeToFilm(createdFilm1.getId(), createdUser1.getId());
        likeStorage.putLikeToFilm(createdFilm1.getId(), createdUser2.getId());
        likeStorage.putLikeToFilm(createdFilm2.getId(), createdUser2.getId());

        Collection<Film> popularFilms = filmStorage.getPopularFilms(2);
        assertNotNull(popularFilms);
        assertEquals(2, popularFilms.size());
    }

    @Test
    void shouldCreateUser() {
        User user = userStorage.createUser(user1);
        assertEquals(user1.getName(), user.getName());
        assertEquals(user1.getEmail(), user.getEmail());
        assertEquals(user1.getLogin(), user.getLogin());
        assertEquals(user1.getBirthday(), user.getBirthday());
    }

    @Test
    void shouldUpdateUser() {
        userStorage.createUser(user1);
        user2.setId(user1.getId());
        User user = userStorage.updateUser(user2);
        assertEquals(user2.getName(), user.getName());
        assertEquals(user2.getEmail(), user.getEmail());
        assertEquals(user2.getLogin(), user.getLogin());
        assertEquals(user2.getBirthday(), user.getBirthday());
    }

    @Test
    void shouldGetUserById() {
        User user = userStorage.createUser(user1);
        Integer userId = user.getId();
        User findedUser = userStorage.getUserById(userId);
        assertEquals(userId, findedUser.getId());
    }

    @Test
    void shouldGetAllUsers() {
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        List<User> newList = List.of(user1, user2);
        List<User> userList = userStorage.getUsers();
        assertEquals(newList.size(), userList.size());
    }

    @Test
    void shouldAddUserToFriendsAndGetFriendsByUser() {
        User createdUser1 = userStorage.createUser(user1);
        User createdUser2 = userStorage.createUser(user2);
        friendStorage.addUserToFriends(createdUser1.getId(), createdUser2.getId());
        List<User> userFriends = userStorage.getFriendsByUser(createdUser1.getId());
        assertNotNull(userFriends);
        assertEquals(1, userFriends.size());

        List<User> anotherUserFriends = userStorage.getFriendsByUser(createdUser2.getId());
        assertNotNull(anotherUserFriends);
        assertEquals(0, anotherUserFriends.size());
    }

    @Test
    void shouldDeleteUserFromFriends() {
        User createdUser1 = userStorage.createUser(user1);
        User createdUser2 = userStorage.createUser(user2);
        friendStorage.addUserToFriends(createdUser1.getId(), createdUser2.getId());
        List<User> userFriends = userStorage.getFriendsByUser(createdUser1.getId());
        assertNotNull(userFriends);
        assertEquals(1, userFriends.size());

        friendStorage.deleteUserFromFriends(createdUser1.getId(), createdUser2.getId());

        List<User> anotherUserFriends = userStorage.getFriendsByUser(createdUser2.getId());
        assertNotNull(anotherUserFriends);
        assertEquals(0, anotherUserFriends.size());
    }
}