package ru.yandex.practicum.filmorate.storage.film_genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface FilmGenreStorage {

    void createFilmGenres(int filmId, Set<Genre> genres);

    void updateFilmGenres(int filmId, Set<Genre> genres);

    Set<Genre> readFilmGenres(int filmId);

}
