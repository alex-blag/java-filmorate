package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreService {

    @Qualifier("genreDbStorage")
    private final GenreDbStorage genreDbStorage;

    public Set<Genre> getGenres() {
        return genreDbStorage.readGenres();
    }

    public Genre getGenre(int genreId) {
        return genreDbStorage
                .readGenre(genreId)
                .orElseThrow(() -> new NoSuchGenreIdException(genreId));
    }

}
