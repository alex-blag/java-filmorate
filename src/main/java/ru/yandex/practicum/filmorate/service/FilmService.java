package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoLikeFromUserIdException;
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

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        filmStorage.createFilm(film);
        return getFilm(film.getId());
    }

    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        return getFilm(film.getId());
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
        var film = getFilm(filmId);
        var user = userStorage
                .getUser(userId)
                .orElseThrow(() -> new NoSuchUserIdException(userId));

        film.getUserIdsLikes().add(userId);
        updateFilm(film);
    }

    public void removeLike(int filmId, int userId) {
        var film = getFilm(filmId);

        var userIdsLikes = film.getUserIdsLikes();
        if (userIdsLikes.contains(userId)) {
            film.getUserIdsLikes().remove(userId);
            updateFilm(film);
        } else {
            throw new NoLikeFromUserIdException(userId);
        }
    }

    public List<Film> getPopularFilms(int numberOfFilms) {
        Comparator<Film> numberOfLikesComparator = Comparator.comparingInt(f -> f.getUserIdsLikes().size());

        return getFilms().stream()
                .sorted(numberOfLikesComparator.reversed())
                .limit(numberOfFilms)
                .collect(Collectors
                        .toList());
    }

}
