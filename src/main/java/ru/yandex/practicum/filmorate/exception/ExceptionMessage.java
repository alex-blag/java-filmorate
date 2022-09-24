package ru.yandex.practicum.filmorate.exception;

public enum ExceptionMessage {

    NO_SUCH_FILM_ID("Net takogo identifikatora filma"),
    NO_SUCH_USER_ID("Net takogo identifikatora polzovatelya");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
