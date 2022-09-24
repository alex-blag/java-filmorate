package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("user@mail");
        user.setLogin("user");
        user.setName(null);
        user.setBirthday(LocalDate.now());
    }

    @Test
    void givenUserWithEmptyName_whenCreateUser_expectNameAsLogin() {
        User created = userController.createUser(user);

        assertEquals(created.getName(), created.getLogin());
    }

    @Test
    void givenUserWithEmptyName_whenUpdateUser_expectNameAsLogin() {
        User created = userController.createUser(user);
        created.setName("");
        User updated = userController.updateUser(created);

        assertEquals(updated.getName(), updated.getLogin());
    }

}
