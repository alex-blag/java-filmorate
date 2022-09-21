package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> userStorage;
    private int id;

    public InMemoryUserStorage() {
        this.userStorage = new HashMap<>();
        this.id = 1;
    }

    @Override
    public User createUser(User user) {
        user.setId(generateId());
        fillNameIfEmpty(user);

        log.info("Create: {}", user);
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        int userId = user.getId();
        validate(userId);

        fillNameIfEmpty(user);

        log.info("Update: {}", user);
        userStorage.put(userId, user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public User getUser(int userId) {
        this.validate(userId);
        return userStorage.get(userId);
    }

    @Override
    public void validate(int userId) {
        if (!userStorage.containsKey(userId)) {
            throw new NoSuchUserIdException();
        }
    }

    private int generateId() {
        return id++;
    }

    private void fillNameIfEmpty(User user) {
        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
