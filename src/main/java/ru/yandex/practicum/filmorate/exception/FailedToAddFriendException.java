package ru.yandex.practicum.filmorate.exception;

public class FailedToAddFriendException extends RuntimeException {

    private final int friendId;

    public FailedToAddFriendException(int friendId) {
        this.friendId = friendId;
    }

    public int getFriendId() {
        return friendId;
    }

}
