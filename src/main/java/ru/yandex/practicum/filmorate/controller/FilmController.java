package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/films")
@RequiredArgsConstructor
@Validated
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        return filmService.getFilm(filmId);
    }

    @PutMapping("{filmId}/like/{userId}")
    public void addLike(
            @PathVariable int filmId,
            @PathVariable int userId
    ) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public void removeLike(
            @PathVariable int filmId,
            @PathVariable int userId
    ) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @Positive
            @RequestParam(defaultValue = "10", required = false)
            int count
    ) {
        return filmService.getPopularFilms(count);
    }

}
