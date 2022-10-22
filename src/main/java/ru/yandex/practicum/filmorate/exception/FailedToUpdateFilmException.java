package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.Film;

public class FailedToUpdateFilmException extends RuntimeException {

    private final Film film;

    public FailedToUpdateFilmException(Film film) {
        this.film = film;
    }

    public Film getFilm() {
        return film;
    }
    
}
