package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public List<User> getFriends(int userId) {
        Set<Integer> friendsIds = userStorage.getUser(userId).getFriendsIds();
        return getUsers(friendsIds);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.getUser(userId);
        User other = userStorage.getUser(otherId);
        Set<Integer> friendsIds = user.getFriendsIds();
        Set<Integer> othersIds = other.getFriendsIds();

        Set<Integer> commonsIds = friendsIds.stream()
                .filter(othersIds::contains)
                .collect(Collectors
                        .toSet());

        return getUsers(commonsIds);
    }

    private List<User> getUsers(Set<Integer> userIds) {
        List<User> users = new ArrayList<>();

        for (int userId : userIds) {
            User user = userStorage.getUser(userId);
            users.add(user);
        }

        return users;
    }

}
