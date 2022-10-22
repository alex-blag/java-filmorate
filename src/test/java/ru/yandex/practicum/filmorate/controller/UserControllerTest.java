package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class UserControllerTest {

    private final UserController userController;

    private User john;

    @BeforeEach
    void setUp() {
        john = new User();
        john.setLogin("john");
        john.setName("John");
        john.setEmail("john@mail.com");
        john.setBirthday(LocalDate.of(1981, 1, 1));
        john.setFriendships(new HashSet<>());
    }

    @Test
    void givenUserWithEmptyName_whenCreateUser_expectNameAsLogin() {
        john.setName(null);
        User created = userController.createUser(john);

        assertEquals(created.getName(), created.getLogin());
    }

    @Test
    void givenUserWithEmptyName_whenUpdateUser_expectNameAsLogin() {
        User created = userController.createUser(john);
        created.setName("");
        User updated = userController.updateUser(created);

        assertEquals(updated.getName(), updated.getLogin());
    }

}
