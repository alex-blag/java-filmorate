package ru.yandex.practicum.filmorate.exception;

public class NoSuchUserIdException extends RuntimeException {

    private final int userId;

    public NoSuchUserIdException(int userId) {
        super();
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

}
