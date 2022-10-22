package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.User;

public class FailedToInsertUserException extends RuntimeException {

    private final User user;

    public FailedToInsertUserException(User user) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
