package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FailedToInsertLikeException;
import ru.yandex.practicum.filmorate.exception.NoSuchLikeToDeleteException;
import ru.yandex.practicum.filmorate.storage.LikeCol;

@Component
@Qualifier("likeDbStorage")
@RequiredArgsConstructor
@Slf4j
public class LikeDbStorage implements LikeStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void createLike(int filmId, int userId) {
        log.info("Create like: {}, {}", filmId, userId);
        insertLike(filmId, userId);
    }

    @Override
    public void destroyLike(int filmId, int userId) {
        log.info("Destroy like: {}, {}", filmId, userId);
        deleteLike(filmId, userId);
    }

    private void insertLike(int filmId, int userId) throws FailedToInsertLikeException {
        String sql = String.format(
                "INSERT INTO _like (film_id, user_id) " +
                        "VALUES (:%s, :%s)",
                LikeCol.FILM_ID,
                LikeCol.USER_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(LikeCol.FILM_ID, filmId)
                .addValue(LikeCol.USER_ID, userId);

        int rowsAffected = jdbcTemplate.update(sql, sqlParams);
        if (rowsAffected == 0) {
            throw new FailedToInsertLikeException(filmId, userId);
        }
    }

    private void deleteLike(int filmId, int userId) throws NoSuchLikeToDeleteException {
        String sql = String.format(
                "DELETE FROM _like " +
                        "WHERE film_id = :%s " +
                        "AND user_id = :%s",
                LikeCol.FILM_ID,
                LikeCol.USER_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(LikeCol.FILM_ID, filmId)
                .addValue(LikeCol.USER_ID, userId);

        int rowsAffected = jdbcTemplate.update(sql, sqlParams);
        if (rowsAffected == 0) {
            throw new NoSuchLikeToDeleteException(filmId, userId);
        }
    }

}
