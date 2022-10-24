package ru.yandex.practicum.filmorate.storage.like;

public interface LikeStorage {

    void createLike(int filmId, int userId);

    void destroyLike(int filmId, int userId);

}
