package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilmId(film.getId());
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(int filmId) {
        return filmStorage
                .getFilm(filmId)
                .orElseThrow(() -> new NoSuchFilmIdException(filmId));
    }

    public void addLike(int filmId, int userId) {
        validateUserId(userId);
        getFilm(filmId).addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        validateUserId(userId);
        getFilm(filmId).removeLike(userId);
    }

    public List<Film> getPopularFilms(int numberOfFilms) {
        Comparator<Film> numberOfLikesComparator = Comparator.comparingInt(f -> f.getUserIdsThatLiked().size());

        return getFilms().stream()
                .sorted(numberOfLikesComparator.reversed())
                .limit(numberOfFilms)
                .collect(Collectors
                        .toList());
    }

    private void validateUserId(int userId) {
        userStorage
                .getUser(userId)
                .orElseThrow(() -> new NoSuchUserIdException(userId));
    }

    private void validateFilmId(int filmId) {
        getFilm(filmId);
    }

}
