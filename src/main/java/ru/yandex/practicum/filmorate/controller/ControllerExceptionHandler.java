package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handle(NoSuchUserIdException e) {
        return getExceptionResponse(
                getNoSuchUserIdExceptionMessage(e.getUserId()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(NoSuchFilmIdException e) {
        return getExceptionResponse(
                formatExceptionMessage(ExceptionMessage.NO_SUCH_FILM_ID, e.getFilmId()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(MethodArgumentNotValidException e) {
        return getExceptionResponse(
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(ConstraintViolationException e) {
        return getExceptionResponse(
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(FailedToInsertFilmException e) {
        return getExceptionResponse(
                e.getFilm().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(FailedToUpdateFilmException e) {
        return getExceptionResponse(
                e.getFilm().toString(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(FailedToInsertUserException e) {
        return getExceptionResponse(
                e.getUser().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(FailedToUpdateUserException e) {
        return getExceptionResponse(
                e.getUser().toString(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(FailedToAddFriendException e) {
        return getExceptionResponse(
                getFailedToAddFriendExceptionMessage(e.getFriendId()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(NoLikeFromUserIdException e) {
        return getExceptionResponse(
                getNoLikeFromUserIdExceptionMessage(e.getUserId()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(NoSuchMpaRatingIdException e) {
        return getExceptionResponse(
                formatExceptionMessage(ExceptionMessage.NO_SUCH_MPA_RATING_ID, e.getMpaRatingId()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(NoSuchGenreIdException e) {
        return getExceptionResponse(
                formatExceptionMessage(ExceptionMessage.NO_SUCH_GENRE_ID, e.getGenreId()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(Throwable e) {
        return getExceptionResponse(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<String> getExceptionResponse(String message, HttpStatus status) {
        log.error(message);
        return new ResponseEntity<>(message, status);
    }

    private String getNoSuchUserIdExceptionMessage(int userId) {
        return formatExceptionMessage(ExceptionMessage.NO_SUCH_USER_ID, userId);
    }

    private String getFailedToAddFriendExceptionMessage(int friendId) {
        return formatExceptionMessage(ExceptionMessage.FAILED_TO_ADD_FRIEND, friendId);
    }

    private String getNoLikeFromUserIdExceptionMessage(int userId) {
        return formatExceptionMessage(ExceptionMessage.NO_LIKE_FROM_USER_ID, userId);
    }

    private String formatExceptionMessage(ExceptionMessage exceptionMessage, int id) {
        return String.format(
                "%s [%d]",
                exceptionMessage.getMessage(),
                id
        );
    }

}
