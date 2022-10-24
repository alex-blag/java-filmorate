package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.User;

public class FailedToUpdateUserException extends RuntimeException {

    private final User user;

    public FailedToUpdateUserException(User user) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
