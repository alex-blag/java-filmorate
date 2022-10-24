package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FailedToDeleteFriendshipException;
import ru.yandex.practicum.filmorate.exception.FailedToInsertFriendshipException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipCol;
import ru.yandex.practicum.filmorate.storage.UserUtils;

import java.util.List;

@Component
@Qualifier("friendshipDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FriendshipDbStorage implements FriendshipStorage {

    private static final String OTHER_ID = "other_id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void createFriendship(int userId, int friendId) {
        log.info("Create friendship: {}, {}", userId, friendId);
        insertFriendship(userId, friendId);
    }

    @Override
    public void destroyFriendship(int userId, int friendId) {
        log.info("Destroy friendship: {}, {}", userId, friendId);
        deleteFriendship(userId, friendId);
    }

    @Override
    public List<User> readFriends(int userId) {
        log.info("Read friends: {}", userId);
        return selectFriends(userId);
    }

    @Override
    public List<User> readCommonFriends(int userId, int otherId) {
        log.info("Read common friends: {}, {}", userId, otherId);
        return selectCommonFriends(userId, otherId);
    }


    private void insertFriendship(int userId, int friendId) throws FailedToInsertFriendshipException {
        String sql = String.format(
                "INSERT INTO friendship (user_id, friend_id, is_confirmed) " +
                        "VALUES (:%s, :%s, :%s)",
                FriendshipCol.USER_ID,
                FriendshipCol.FRIEND_ID,
                FriendshipCol.IS_CONFIRMED);

        var sqlParams = new MapSqlParameterSource()
                .addValue(FriendshipCol.USER_ID, userId)
                .addValue(FriendshipCol.FRIEND_ID, friendId)
                .addValue(FriendshipCol.IS_CONFIRMED, true);

        int rowsAffected = jdbcTemplate.update(sql, sqlParams);
        if (rowsAffected == 0) {
            throw new FailedToInsertFriendshipException(userId, friendId);
        }
    }

    private void deleteFriendship(int userId, int friendId) throws FailedToDeleteFriendshipException {
        String sql = String.format(
                "DELETE FROM friendship " +
                        "WHERE user_id = :%s " +
                        "AND friend_id = :%s",
                FriendshipCol.USER_ID,
                FriendshipCol.FRIEND_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(FriendshipCol.USER_ID, userId)
                .addValue(FriendshipCol.FRIEND_ID, friendId);

        int rowsAffected = jdbcTemplate.update(sql, sqlParams);
        if (rowsAffected == 0) {
            throw new FailedToDeleteFriendshipException(userId, friendId);
        }
    }

    private List<User> selectFriends(int userId) {
        String sql = String.format(
                "SELECT u.user_id, " +
                        "u.login, " +
                        "u.name, " +
                        "u.email, " +
                        "u.birthday " +
                        "FROM friendship f " +
                        "LEFT JOIN _user u ON u.user_id = f.friend_id " +
                        "WHERE f.user_id = :%s",
                FriendshipCol.USER_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(FriendshipCol.USER_ID, userId);

        return jdbcTemplate.query(sql, sqlParams, UserUtils::mapUser);
    }

    private List<User> selectCommonFriends(int userId, int otherId) {
        String sql = String.format(
                "WITH common_friends_ids AS (" +
                        "SELECT f1.friend_id " +
                        "FROM friendship f1 " +
                        "WHERE f1.user_id = :%s " +
                        "INTERSECT " +
                        "SELECT f2.friend_id " +
                        "FROM friendship f2 " +
                        "WHERE f2.user_id = :%s " +
                        ") " +
                        "SELECT u.user_id, " +
                        "u.login, " +
                        "u.name, " +
                        "u.email, " +
                        "u.birthday " +
                        "FROM common_friends_ids cfi " +
                        "LEFT JOIN _user u ON u.user_id = cfi.friend_id",
                FriendshipCol.USER_ID,
                OTHER_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(FriendshipCol.USER_ID, userId)
                .addValue(OTHER_ID, otherId);

        return jdbcTemplate.query(sql, sqlParams, UserUtils::mapUser);
    }

}
