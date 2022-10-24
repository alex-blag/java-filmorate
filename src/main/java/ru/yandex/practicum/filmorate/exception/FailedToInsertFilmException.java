package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.Film;

public class FailedToInsertFilmException extends RuntimeException {

    private final Film film;

    public FailedToInsertFilmException(Film film) {
        this.film = film;
    }

    public Film getFilm() {
        return film;
    }

}
