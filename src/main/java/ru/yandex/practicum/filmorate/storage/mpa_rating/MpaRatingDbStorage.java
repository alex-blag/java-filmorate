package ru.yandex.practicum.filmorate.storage.mpa_rating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingIdException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingCol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("mpaRatingDbStorage")
@RequiredArgsConstructor
@Slf4j
public class MpaRatingDbStorage implements MpaRatingStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<MpaRating> readMpaRatings() {
        log.info("Read MPA ratings: ");
        return selectMpaRatings();
    }

    @Override
    public Optional<MpaRating> readMpaRating(int mpaRatingId) {
        log.info("Read MPA rating: {}", mpaRatingId);
        return selectMpaRating(mpaRatingId);
    }

    private List<MpaRating> selectMpaRatings() {
        String sql = "SELECT * " +
                "FROM mpa_rating mr " +
                "ORDER BY mr.mpa_rating_id";

        return jdbcTemplate.query(sql, this::mapMpaRating);
    }

    private Optional<MpaRating> selectMpaRating(int mpaRatingId) throws NoSuchMpaRatingIdException {
        String sql = String.format(
                "SELECT mr.mpa_rating_id, " +
                        "mr.mpa_rating " +
                        "FROM mpa_rating mr " +
                        "WHERE mr.mpa_rating_id = :%s",
                MpaRatingCol.MPA_RATING_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(MpaRatingCol.MPA_RATING_ID, mpaRatingId);

        try {
            var mpaRating = jdbcTemplate.queryForObject(sql, sqlParams, this::mapMpaRating);
            return Optional.ofNullable(mpaRating);

        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchMpaRatingIdException(mpaRatingId);
        }
    }

    private MpaRating mapMpaRating(ResultSet rs, int rowNum) throws SQLException {
        MpaRating mr = new MpaRating();
        mr.setId(rs.getInt(MpaRatingCol.MPA_RATING_ID));
        mr.setName(rs.getString(MpaRatingCol.MPA_RATING));
        return mr;
    }

}
