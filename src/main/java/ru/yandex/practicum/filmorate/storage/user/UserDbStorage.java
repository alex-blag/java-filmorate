package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FailedToInsertUserException;
import ru.yandex.practicum.filmorate.exception.FailedToUpdateUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserCol;
import ru.yandex.practicum.filmorate.storage.UserUtils;

import java.util.List;
import java.util.Optional;

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
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Update user: {}", user);
        updateSetUser(user);
        return user;
    }

    @Override
    public List<User> readUsers() {
        log.info("Read users: ");
        return selectUsers();
    }

    @Override
    public Optional<User> readUser(int userId) {
        log.info("Read user: {}", userId);
        var user = selectUser(userId);
        return Optional.ofNullable(user);
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

    private void updateSetUser(User user) throws FailedToUpdateUserException {
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

    private List<User> selectUsers() {
        String sql = "SELECT * " +
                "FROM _user";

        return namedJdbcTemplate.query(sql, UserUtils::mapUser);
    }

    private User selectUser(int userId) {
        String sql = String.format(
                "SELECT * " +
                        "FROM _user u " +
                        "WHERE u.user_id = :%s",
                UserCol.USER_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(UserCol.USER_ID, userId);

        var users = namedJdbcTemplate.query(sql, sqlParams, UserUtils::mapUser);
        return users.size() == 1
                ? users.get(0)
                : null;
    }

}
