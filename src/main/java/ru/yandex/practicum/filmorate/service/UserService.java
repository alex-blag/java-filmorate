package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

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

        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getFriends(int userId) {
        return getUser(userId)
                .getFriendsIds().stream()
                .map(this::getUser)
                .collect(Collectors
                        .toList());
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        Set<Integer> friendsIds = getUser(userId).getFriendsIds();
        Set<Integer> othersIds = getUser(otherId).getFriendsIds();

        return friendsIds.stream()
                .filter(othersIds::contains)
                .map(this::getUser)
                .collect(Collectors
                        .toList());
    }

    private void validateUserId(int userId) {
        getUser(userId);
    }

}
