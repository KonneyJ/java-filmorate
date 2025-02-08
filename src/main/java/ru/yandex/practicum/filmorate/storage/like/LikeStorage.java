package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {

    void putLikeToFilm(Integer filmId, Integer userId);

    void deleteLikeFromFilm(Integer filmId, Integer userId);

    List<Like> getLikesByFilm(Integer id);

}
