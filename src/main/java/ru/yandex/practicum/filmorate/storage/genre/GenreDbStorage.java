package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreCol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("genreDbStorage")
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenreStorage {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        log.info("Get genres: ");

        return selectGenres();
    }

    @Override
    public Optional<Genre> getGenre(int genreId) {
        log.info("Get genre: {}", genreId);

        return selectGenre(genreId);
    }

    private List<Genre> selectGenres() {
        String sql = "SELECT * " +
                "FROM genre g " +
                "ORDER BY g.genre_id";

        return namedJdbcTemplate.query(sql, this::mapGenre);
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
            var genre = namedJdbcTemplate.queryForObject(sql, sqlParams, this::mapGenre);
            return Optional.ofNullable(genre);

        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchGenreIdException(genreId);
        }
    }

    private Genre mapGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt(GenreCol.GENRE_ID));
        genre.setGenre(rs.getString(GenreCol.GENRE));
        return genre;
    }

}
