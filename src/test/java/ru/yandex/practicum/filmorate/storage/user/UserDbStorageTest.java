package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

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
class UserDbStorageTest {

    private final UserDbStorage userDbStorage;

    private User john;
    private User jane;

    @BeforeEach
    void setUp() {
        john = new User();
        john.setLogin("john");
        john.setName("John");
        john.setEmail("john@mail.com");
        john.setBirthday(LocalDate.of(1981, 1, 1));
        john.setFriendships(new HashSet<>());

        jane = new User();
        jane.setLogin("jane");
        jane.setName("Jane");
        jane.setEmail("jane@mail.com");
        jane.setBirthday(LocalDate.of(1982, 12, 31));
        jane.setFriendships(new HashSet<>());
    }

    @Test
    void givenNewUser_whenCreateUser_expectActualUserId() {
        var created = userDbStorage.createUser(john);
        assertEquals(1, created.getId());
    }

    @Test
    void givenExistingUser_whenCreateUser_expectException() {
        var created = userDbStorage.createUser(john);

        assertThrows(
                DuplicateKeyException.class,
                () -> userDbStorage.createUser(created)
        );
    }

    @Test
    void givenUser_whenUpdateUser_expectUpdatedUser() {
        var created = userDbStorage.createUser(john);

        String newName = "Johnny";
        created.setName(newName);

        var updated = userDbStorage.updateUser(created);
        assertEquals(newName, updated.getName());
    }

    @Test
    void given2Users_whenGetUsers_expectActual2Users() {
        var created1 = userDbStorage.createUser(john);
        var created2 = userDbStorage.createUser(jane);
        var created = List.of(created1, created2);

        var gotten = userDbStorage.getUsers();
        assertEquals(created, gotten);
    }

    @Test
    void givenUser_whenGetUser_expectActualUser() {
        var created = userDbStorage.createUser(john);

        var gotten = userDbStorage.getUser(created.getId()).orElseThrow();
        assertEquals(created, gotten);
    }

}