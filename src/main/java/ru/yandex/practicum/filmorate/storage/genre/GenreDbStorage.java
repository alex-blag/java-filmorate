package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreCol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Qualifier("genreDbStorage")
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenreStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Set<Genre> readGenres() {
        log.info("Read genres: ");
        return selectGenres();
    }

    @Override
    public Optional<Genre> readGenre(int genreId) {
        log.info("Read genre: {}", genreId);
        return selectGenre(genreId);
    }

    private Set<Genre> selectGenres() {
        String sql = "SELECT * " +
                "FROM genre g " +
                "ORDER BY g.genre_id";

        return jdbcTemplate.query(sql, this::extractGenres);
    }

    private Optional<Genre> selectGenre(int genreId) {
        String sql = String.format(
                "SELECT g.genre_id, " +
                        "g.genre " +
                        "FROM genre g " +
                        "WHERE g.genre_id = :%s",
                GenreCol.GENRE_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(GenreCol.GENRE_ID, genreId);

        try {
            var genre = jdbcTemplate.queryForObject(sql, sqlParams, this::mapGenre);
            return Optional.ofNullable(genre);

        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchGenreIdException(genreId);
        }
    }

    private Set<Genre> extractGenres(ResultSet rs) throws SQLException, DataAccessException {
        Set<Genre> genres = new HashSet<>();
        while (rs.next()) {
            var g = new Genre();
            g.setId(rs.getInt(GenreCol.GENRE_ID));
            g.setName(rs.getString(GenreCol.GENRE));
            genres.add(g);
        }
        return genres;
    }

    private Genre mapGenre(ResultSet rs, int rowNum) throws SQLException {
        var g = new Genre();
        g.setId(rs.getInt(GenreCol.GENRE_ID));
        g.setName(rs.getString(GenreCol.GENRE));
        return g;
    }

}
