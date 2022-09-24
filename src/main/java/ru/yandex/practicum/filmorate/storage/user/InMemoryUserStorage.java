package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        log.info("Create: {}", user);
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Update: {}", user);
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public Optional<User> getUser(int userId) {
        return Optional.ofNullable(userStorage.get(userId));
    }

    private int generateId() {
        return id++;
    }

}
