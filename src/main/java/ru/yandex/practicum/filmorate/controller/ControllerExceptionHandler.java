package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ExceptionMessage;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;

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
                getNoSuchFilmIdExceptionMessage(e.getFilmId()),
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

    private String getNoSuchFilmIdExceptionMessage(int filmId) {
        return getExceptionMessage(ExceptionMessage.NO_SUCH_FILM_ID, filmId);
    }

    private String getNoSuchUserIdExceptionMessage(int userId) {
        return getExceptionMessage(ExceptionMessage.NO_SUCH_USER_ID, userId);
    }

    private String getExceptionMessage(ExceptionMessage exceptionMessage, int id) {
        return String.format(
                "%s [%d]",
                exceptionMessage.getMessage(),
                id
        );
    }

}
