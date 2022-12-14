package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Qualifier("friendshipDbStorage")
    private final FriendshipStorage friendshipStorage;

    public User postUser(User user) {
        return userStorage.createUser(user);
    }

    public User putUser(User user) {
        validateUserId(user.getId());
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.readUsers();
    }

    public User getUser(int userId) {
        return userStorage
                .readUser(userId)
                .orElseThrow(() -> new NoSuchUserIdException(userId));
    }

    public void putFriend(int userId, int friendId) {
        validateUserId(userId);
        validateUserId(friendId);
        friendshipStorage.createFriendship(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        validateUserId(userId);
        validateUserId(friendId);
        friendshipStorage.destroyFriendship(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        validateUserId(userId);
        return friendshipStorage.readFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        validateUserId(userId);
        validateUserId(otherId);
        return friendshipStorage.readCommonFriends(userId, otherId);
    }

    private void validateUserId(int userId) {
        getUser(userId);
    }

}
