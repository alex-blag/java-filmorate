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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    private Film fourRooms;
    private Film starWars;

    private User john;
    private User jane;

    @BeforeEach
    void setUp() {
        fourRooms = new Film();
        fourRooms.setName("Four Rooms");
        fourRooms.setDescription("Four Rooms");
        fourRooms.setReleaseDate(LocalDate.of(1995, 12, 9));
        fourRooms.setDuration(98);
        fourRooms.setUserIdsLikes(new HashSet<>());

        starWars = new Film();
        starWars.setName("Star Wars");
        starWars.setDescription("Star Wars");
        starWars.setReleaseDate(LocalDate.of(1977, 5, 25));
        starWars.setDuration(121);
        starWars.setUserIdsLikes(new HashSet<>());

        john = new User();
        john.setLogin("john.doe");
        john.setName("John Doe");
        john.setEmail("john.doe@mail.com");
        john.setBirthday(LocalDate.of(1981, 1, 1));
        john.setFriendships(new HashSet<>());

        jane = new User();
        jane.setLogin("jane.roe");
        jane.setName("Jane Roe");
        jane.setEmail("jane.roe@mail.com");
        jane.setBirthday(LocalDate.of(1982, 12, 31));
        jane.setFriendships(new HashSet<>());
    }

    @Test
    void givenFilm_whenCreateFilm_expectActualFilmId() {
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
        var created2 = filmDbStorage.createFilm(starWars);
        var created = List.of(created1, created2);

        var gotten = filmDbStorage.getFilms();
        assertEquals(created, gotten);
    }

    @Test
    void givenFilmWithLikes_whenGetFilm_expectActualFilm() {
        var created = filmDbStorage.createFilm(fourRooms);

        var gotten = filmDbStorage.getFilm(created.getId()).orElseThrow();
        assertEquals(created, gotten);
    }

}
