package ru.yandex.practicum.filmorate.exception;

public class FailedToInsertLikeException extends RuntimeException {

    private final int filmId;
    private final int userId;

    public FailedToInsertLikeException(int filmId, int userId) {
        this.filmId = filmId;
        this.userId = userId;
    }

    public int getFilmId() {
        return filmId;
    }

    public int getUserId() {
        return userId;
    }

}
