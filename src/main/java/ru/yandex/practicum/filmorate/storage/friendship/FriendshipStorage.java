package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {

    void createFriendship(int userId, int friendId);

    void destroyFriendship(int userId, int friendId);

    List<User> readFriends(int userId);

    List<User> readCommonFriends(int userId, int otherId);

}
