package ru.yandex.practicum.filmorate.exception;

public class FailedToDeleteFriendshipException extends RuntimeException {

    private final int userId;
    private final int friendId;

    public FailedToDeleteFriendshipException(int userId, int friendId) {
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
