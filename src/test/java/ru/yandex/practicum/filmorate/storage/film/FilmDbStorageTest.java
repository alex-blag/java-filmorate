package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;

    private Film fourRooms;
    private Film starWars;

    @BeforeEach
    void setUp() {
        fourRooms = new Film();
        fourRooms.setName("Four Rooms");
        fourRooms.setDescription("Four Rooms");
        fourRooms.setReleaseDate(LocalDate.of(1995, 12, 9));
        fourRooms.setDuration(98);
        MpaRating mpaRating = new MpaRating();
        mpaRating.setId(4);
        fourRooms.setMpaRating(mpaRating);
        Genre genre = new Genre();
        genre.setId(1);
        fourRooms.setGenres(Set.of(genre));
        fourRooms.setUserIdsLikes(new HashSet<>());
        fourRooms.setRank(10);

        starWars = new Film();
        starWars.setName("Star Wars");
        starWars.setDescription("Star Wars");
        starWars.setReleaseDate(LocalDate.of(1977, 5, 25));
        starWars.setDuration(121);
        mpaRating = new MpaRating();
        mpaRating.setId(2);
        starWars.setMpaRating(mpaRating);
        genre = new Genre();
        genre.setId(6);
        starWars.setGenres(Set.of(genre));
        starWars.setUserIdsLikes(new HashSet<>());
        starWars.setRank(1);
    }

    @Test
    void givenFilm_whenCreateFilm_expectActualFilm() {
        var created = filmDbStorage.createFilm(fourRooms);
        assertEquals(1, created.getId());
    }

    @Test
    void givenExistingFilm_whenCreateFilm_expectException() {
        var created = filmDbStorage.createFilm(fourRooms);

        assertThrows(
                DuplicateKeyException.class,
                () -> filmDbStorage.createFilm(created)
        );
    }

    @Test
    void givenFilm_whenUpdateFilm_expectUpdatedFilm() {
        var created = filmDbStorage.createFilm(fourRooms);

        String newName = "4 Rooms";
        created.setName(newName);

        var updated = filmDbStorage.updateFilm(created);
        assertEquals(newName, updated.getName());
    }

    @Test
    void givenFilms_whenGetFilms_expectActualFilms() {
        var created1 = filmDbStorage.createFilm(fourRooms);
        var gotten1 = filmDbStorage.getFilm(created1.getId()).orElseThrow();

        var created2 = filmDbStorage.createFilm(starWars);
        var gotten2 = filmDbStorage.getFilm(created2.getId()).orElseThrow();

        var created = List.of(gotten1, gotten2);

        var gotten = filmDbStorage.getFilms();
        assertEquals(created, gotten);
    }

    @Test
    void givenFilmWithLikes_whenGetFilm_expectActualFilm() {
        var created = filmDbStorage.createFilm(fourRooms);
        var gotten = filmDbStorage.getFilm(created.getId()).orElseThrow();
        assertEquals(1, gotten.getId());
    }

}
