package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> readFilm(int filmId);

    List<Film> readFilms();

    List<Film> readPopularFilms(int numberOfFilms);

}
