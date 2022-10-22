package ru.yandex.practicum.filmorate.exception;

public class NoSuchGenreIdException extends RuntimeException {

    private final int genreId;

    public NoSuchGenreIdException(int genreId) {
        super();
        this.genreId = genreId;
    }

    public int getGenreId() {
        return genreId;
    }

}
