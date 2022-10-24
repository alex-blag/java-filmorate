package ru.yandex.practicum.filmorate.storage.friendship;

import java.util.List;

public interface FriendshipStorage {

    void createFriendship(int userId, int friendId);

    void destroyFriendship(int userId, int friendId);

    List<Integer> readFriendsIds(int userId);

    List<Integer> readCommonFriendsIds(int userId, int otherId);

}
