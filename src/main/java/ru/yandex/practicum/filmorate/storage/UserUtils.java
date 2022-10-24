package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserUtils {

    private UserUtils() {
    }

    public static User mapUser(ResultSet rs, int rowNum) throws SQLException {
        var user = new User();
        user.setId(rs.getInt(UserCol.USER_ID));
        user.setLogin(rs.getString(UserCol.LOGIN));
        user.setName(rs.getString(UserCol.NAME));
        user.setEmail(rs.getString(UserCol.EMAIL));
        user.setBirthday(rs.getDate(UserCol.BIRTHDAY).toLocalDate());
        return user;
    }

}
