package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        userStorage.validate(userId);
        Film film = filmStorage.getFilm(filmId);
        film.addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        userStorage.validate(userId);
        Film film = filmStorage.getFilm(filmId);
        film.removeLike(userId);
    }

    public List<Film> getPopularFilms(int numberOfFilms) {
        Comparator<Film> numberOfLikesComparator = Comparator.comparingInt(f -> f.getUserIdsThatLiked().size());

        return filmStorage.getFilms().stream()
                .sorted(numberOfLikesComparator.reversed())
                .limit(numberOfFilms)
                .collect(Collectors
                        .toList());
    }

}
