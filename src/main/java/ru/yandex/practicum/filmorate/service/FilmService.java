package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film_genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;

    @Qualifier("mpaRatingDbStorage")
    private final MpaRatingStorage mpaRatingStorage;

    @Qualifier("filmGenreDbStorage")
    private final FilmGenreStorage filmGenreStorage;

    @Qualifier("likeDbStorage")
    private final LikeStorage likeStorage;

    public Film postFilm(Film film) {
        filmStorage.createFilm(film);
        filmGenreStorage.createFilmGenres(film.getId(), film.getGenres());
        return getFilm(film.getId());
    }

    public Film putFilm(Film film) {
        filmStorage.updateFilm(film);
        filmGenreStorage.updateFilmGenres(film.getId(), film.getGenres());
        return getFilm(film.getId());
    }

    public Film getFilm(int filmId) {
        var film = filmStorage
                .readFilm(filmId)
                .orElseThrow(() -> new NoSuchFilmIdException(filmId));

        setMpaRating(film);
        setGenres(film);
        return film;
    }

    public List<Film> getFilms() {
        var films = filmStorage.readFilms();
        films.forEach(this::setMpaRating);
        films.forEach(this::setGenres);
        return films;
    }

    public List<Film> getPopularFilms(int numberOfFilms) {
        return filmStorage.readPopularFilms(numberOfFilms);
    }

    public void putLike(int filmId, int userId) {
        likeStorage.createLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        likeStorage.destroyLike(filmId, userId);
    }

    private void setMpaRating(Film film) {
        var mpaRatingId = film.getMpaRating().getId();
        var mpaRating = getMpaRating(mpaRatingId);
        film.setMpaRating(mpaRating);
    }

    private MpaRating getMpaRating(int mpaRatingId) {
        return mpaRatingStorage
                .readMpaRating(mpaRatingId)
                .orElseThrow(() -> new NoSuchMpaRatingIdException(mpaRatingId));
    }

    private void setGenres(Film film) {
        var filmId = film.getId();
        var filmGenres = filmGenreStorage.readFilmGenres(filmId);
        film.setGenres(filmGenres);
    }

}
