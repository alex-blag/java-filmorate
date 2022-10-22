package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FailedToInsertUserException;
import ru.yandex.practicum.filmorate.exception.FailedToUpdateUserException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipCol;
import ru.yandex.practicum.filmorate.storage.UserCol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Qualifier("userDbStorage")
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public User createUser(User user) {
        log.info("Create user: {}", user);

        insertUser(user);
        insertFriends(user.getId(), user.getFriendships());
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Update user: {}", user);

        _updateUser(user);
        updateFriends(user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        log.info("Get users: ");

        var users = selectUsers();
        for (User user : users) {
            var friends = selectFriends(user.getId());
            user.setFriendships(friends);
        }
        return users;
    }

    @Override
    public Optional<User> getUser(int userId) {
        log.info("Get user by id: {}", userId);

        var user = selectUser(userId);
        if (user == null) {
            return Optional.empty();
        } else {
            var friends = selectFriends(user.getId());
            user.setFriendships(friends);
            return Optional.of(user);
        }
    }

    private void insertUser(User user) throws FailedToInsertUserException {
        String sql = String.format(
                "INSERT INTO _user (login, name, email, birthday) " +
                        "VALUES (:%s, :%s, :%s, :%s)",
                UserCol.LOGIN,
                UserCol.NAME,
                UserCol.EMAIL,
                UserCol.BIRTHDAY);

        var sqlParams = new MapSqlParameterSource()
                .addValue(UserCol.LOGIN, user.getLogin())
                .addValue(UserCol.NAME, user.getName())
                .addValue(UserCol.EMAIL, user.getEmail())
                .addValue(UserCol.BIRTHDAY, user.getBirthday());

        var keyHolder = new GeneratedKeyHolder();

        int rowsAffected = namedJdbcTemplate.update(sql, sqlParams, keyHolder);
        if (rowsAffected == 0 || keyHolder.getKey() == null) {
            throw new FailedToInsertUserException(user);
        }

        user.setId(keyHolder.getKey().intValue());
    }

    private void _updateUser(User user) throws FailedToUpdateUserException {
        String sql = String.format(
                "UPDATE _user u " +
                        "SET u.login = :%s, " +
                        "u.name = :%s, " +
                        "u.email = :%s, " +
                        "u.birthday = :%s " +
                        "WHERE u.user_id = :%s",
                UserCol.LOGIN,
                UserCol.NAME,
                UserCol.EMAIL,
                UserCol.BIRTHDAY,
                UserCol.USER_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(UserCol.LOGIN, user.getLogin())
                .addValue(UserCol.NAME, user.getName())
                .addValue(UserCol.EMAIL, user.getEmail())
                .addValue(UserCol.BIRTHDAY, user.getBirthday())
                .addValue(UserCol.USER_ID, user.getId());

        int rowsAffected = namedJdbcTemplate.update(sql, sqlParams);
        if (rowsAffected == 0) {
            throw new FailedToUpdateUserException(user);
        }
    }

    private void insertFriends(int userId, Set<Friendship> friends) {
        String sql = String.format(
                "INSERT INTO friendship (user_id, friend_id, is_confirmed)" +
                        "VALUES (:%s, :%s, :%s)",
                FriendshipCol.USER_ID,
                FriendshipCol.FRIEND_ID,
                FriendshipCol.IS_CONFIRMED);

        var sqlParams = friends.stream()
                .map(f -> new MapSqlParameterSource()
                        .addValue(FriendshipCol.USER_ID, userId)
                        .addValue(FriendshipCol.FRIEND_ID, f.getFriendId())
                        .addValue(FriendshipCol.IS_CONFIRMED, f.isConfirmed()))
                .toArray(SqlParameterSource[]::new);

        namedJdbcTemplate.batchUpdate(sql, sqlParams);
    }

    private void updateFriends(User user) {
        deleteFriends(user.getId());
        insertFriends(user.getId(), user.getFriendships());
    }

    private void deleteFriends(int userId) {
        String sql = String.format(
                "DELETE FROM friendship f " +
                        "WHERE f.user_id = :%s",
                FriendshipCol.USER_ID);

        var sqlParams = new MapSqlParameterSource(
                FriendshipCol.USER_ID, userId);

        namedJdbcTemplate.update(sql, sqlParams);
    }

    private Set<Friendship> selectFriends(int userId) {
        String sql = String.format(
                "SELECT * " +
                        "FROM friendship f " +
                        "WHERE f.user_id = :%s",
                FriendshipCol.USER_ID);

        var sqlParams = new MapSqlParameterSource(
                FriendshipCol.USER_ID, userId);

        return new HashSet<>(namedJdbcTemplate.query(sql, sqlParams, this::mapFriend));
    }

    private List<User> selectUsers() {
        String sql = "SELECT * " +
                "FROM _user";

        return namedJdbcTemplate.query(sql, this::mapUser);
    }

    private User selectUser(int userId) {
        String sql = String.format(
                "SELECT * " +
                        "FROM _user u " +
                        "WHERE u.user_id = :%s",
                UserCol.USER_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(UserCol.USER_ID, userId);

        var users = namedJdbcTemplate.query(sql, sqlParams, this::mapUser);
        return users.size() == 1
                ? users.get(0)
                : null;
    }

    private User mapUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt(UserCol.USER_ID));
        user.setLogin(rs.getString(UserCol.LOGIN));
        user.setName(rs.getString(UserCol.NAME));
        user.setEmail(rs.getString(UserCol.EMAIL));
        user.setBirthday(rs.getDate(UserCol.BIRTHDAY).toLocalDate());
        return user;
    }

    private Friendship mapFriend(ResultSet rs, int rowNum) throws SQLException {
        return new Friendship(
                rs.getInt(FriendshipCol.FRIEND_ID),
                rs.getBoolean(FriendshipCol.IS_CONFIRMED));
    }

}
