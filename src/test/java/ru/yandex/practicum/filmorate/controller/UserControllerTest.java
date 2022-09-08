package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {

    private UserController controller;
    private User user;

    @BeforeEach
    void setUp() {
        controller = new UserController();

        user = new User();
        user.setId(1);
        user.setEmail("user@mail");
        user.setLogin("user");
        user.setName(null);
        user.setBirthday(LocalDate.now());
    }

    @Test
    void givenUserWithEmptyName_whenCreateUser_expectNameAsLogin() {
        User created = controller.createUser(user);

        assertEquals(created.getName(), created.getLogin());
    }

    @Test
    void givenUserWithEmptyName_whenUpdateUser_expectNameAsLogin() {
        User created = controller.createUser(user);
        created.setName("");
        User updated = controller.updateUser(created);

        assertEquals(updated.getName(), updated.getLogin());
    }
}