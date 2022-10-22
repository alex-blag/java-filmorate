package ru.yandex.practicum.filmorate.exception;

public class NoSuchMpaRatingIdException extends RuntimeException {

    private final int mpaRatingId;

    public NoSuchMpaRatingIdException(int mpaRatingId) {
        this.mpaRatingId = mpaRatingId;
    }

    public int getMpaRatingId() {
        return mpaRatingId;
    }

}
