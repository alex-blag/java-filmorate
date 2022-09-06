package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping
    User createUser(@Valid @RequestBody User user) {
        log.info("Create: {}", user);
        user.setId(generateId());
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    User updateUser(@Valid @RequestBody User user) {
        int userId = user.getId();
        if (!users.containsKey(userId)) {
            throw new NoSuchUserIdException();
        }

        log.info("Update: {}", user);
        users.put(userId, user);
        return user;
    }

    @GetMapping
    Collection<User> getUsers() {
        return users.values();
    }

    private int generateId() {
        return id++;
    }

}
