package ru.yandex.practicum.filmorate.exception;

public enum ExceptionMessage {

    NO_SUCH_FILM_ID("Net takogo identifikatora filma"),
    NO_SUCH_USER_ID("Net takogo identifikatora polzovatelya"),
    FAILED_TO_CREATE_USER("Ne udalos sozdat polzovatelya"),
    FAILED_TO_ADD_FRIEND("Ne udalos dobavit druga"),
    NO_LIKE_FROM_USER_ID("Net layka ot polzovatelya s tekushchim identifikatorom"),
    NO_SUCH_GENRE_ID("Net takogo identifikatora zhanra"),
    NO_SUCH_MPA_RATING_ID("Net takogo identifikatora MPA reytinga");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
