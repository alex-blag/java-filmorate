package ru.yandex.practicum.filmorate.exception;

public class NoSuchFilmIdException extends RuntimeException {

    private final int filmId;

    public NoSuchFilmIdException(int filmId) {
        super();
        this.filmId = filmId;
    }

    public int getFilmId() {
        return filmId;
    }

}
