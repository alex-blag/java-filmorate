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
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        fillUserNameIfEmpty(user);
        return userService.postUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        fillUserNameIfEmpty(user);
        return userService.putUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void putFriend(
            @PathVariable int userId,
            @PathVariable int friendId
    ) {
        userService.putFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(
            @PathVariable int userId,
            @PathVariable int friendId
    ) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable int userId,
            @PathVariable int otherId
    ) {
        return userService.getCommonFriends(userId, otherId);
    }

    private void fillUserNameIfEmpty(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
