package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/films")
public class FilmController {

    final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    Film createFilm(@Valid @RequestBody Film film) {
        log.info("Create: {}", film);
        film.setId(Generator.getId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    Film updateFilm(@Valid @RequestBody Film film) {
        int userId = film.getId();
        if (!films.containsKey(userId)) {
            throw new NoSuchFilmIdException();
        }

        log.info("Update: {}", film);
        films.put(userId, film);
        return film;
    }

    @GetMapping
    Collection<Film> getFilms() {
        return films.values();
    }

}
