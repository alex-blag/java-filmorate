package ru.yandex.practicum.filmorate.exception;

public class FailedToInsertFriendshipException extends RuntimeException {

    private final int userId;
    private final int friendId;

    public FailedToInsertFriendshipException(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public int getUserId() {
        return userId;
    }

    public int getFriendId() {
        return friendId;
    }

}
