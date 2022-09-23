package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> filmStorage;
    private int id;

    public InMemoryFilmStorage() {
        this.filmStorage = new HashMap<>();
        this.id = 1;
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(generateId());

        log.info("Create: {}", film);
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film.getId());

        log.info("Update: {}", film);
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.values());
    }

    @Override
    public Optional<Film> getFilm(int filmId) {
        return Optional.ofNullable(filmStorage.get(filmId));
    }

    private int generateId() {
        return id++;
    }

    private void validate(int filmId) {
        getFilm(filmId)
                .orElseThrow(() -> new NoSuchFilmIdException(filmId));
    }

}
