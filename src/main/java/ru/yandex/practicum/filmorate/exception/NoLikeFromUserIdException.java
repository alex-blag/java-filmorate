package ru.yandex.practicum.filmorate.exception;

public class NoLikeFromUserIdException extends RuntimeException {

    private final int userId;

    public NoLikeFromUserIdException(int userId) {
        super();
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

}
