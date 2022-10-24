package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    Set<Genre> readGenres();

    Optional<Genre> readGenre(int genreId);

}
