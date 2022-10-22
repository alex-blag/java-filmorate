package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUserId(user.getId());
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int userId) {
        return userStorage
                .getUser(userId)
                .orElseThrow(() -> new NoSuchUserIdException(userId));
    }

    public void addFriend(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        Friendship f = new Friendship(friendId, true);
        user.getFriendships().add(f);
        updateUser(user);
    }

    public void removeFriend(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        Friendship f = new Friendship(friendId, true);
        user.getFriendships().remove(f);
        updateUser(user);
    }

    public List<User> getFriends(int userId) {
        return getUser(userId)
                .getFriendships().stream()
                .map(f -> getUser(f.getFriendId()))
                .collect(Collectors
                        .toList());
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        Set<Friendship> friendships = getUser(userId).getFriendships();
        Set<Friendship> others = getUser(otherId).getFriendships();

        return friendships.stream()
                .filter(others::contains)
                .map(f -> getUser(f.getFriendId()))
                .collect(Collectors
                        .toList());
    }

    private void validateUserId(int userId) {
        getUser(userId);
    }

}
