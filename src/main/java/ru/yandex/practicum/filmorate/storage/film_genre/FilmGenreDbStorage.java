package ru.yandex.practicum.filmorate.storage.film_genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmGenreCol;
import ru.yandex.practicum.filmorate.storage.GenreCol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
@Qualifier("filmGenreDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmGenreDbStorage implements FilmGenreStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void createFilmGenres(int filmId, Set<Genre> genres) {
        log.info("Create film genres: {}, {}", filmId, genres);
        insertFilmGenres(filmId, genres);
    }

    @Override
    public void updateFilmGenres(int filmId, Set<Genre> genres) {
        log.info("Update film genres: {}, {}", filmId, genres);
        destroyFilmGenres(filmId);
        insertFilmGenres(filmId, genres);
    }

    @Override
    public Set<Genre> readFilmGenres(int filmId) {
        log.info("Read film genres: {}", filmId);
        return selectFilmGenres(filmId);
    }

    private void insertFilmGenres(int filmId, Set<Genre> genres) {
        String sql = String.format(
                "INSERT INTO film_genre (film_id, genre_id) " +
                        "VALUES (:%s, :%s)",
                FilmGenreCol.FILM_ID,
                FilmGenreCol.GENRE_ID);

        var sqlParams = genres.stream()
                .map(Genre::getId)
                .map(genreId -> new MapSqlParameterSource()
                        .addValue(FilmGenreCol.FILM_ID, filmId)
                        .addValue(FilmGenreCol.GENRE_ID, genreId))
                .toArray(SqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, sqlParams);
    }

    private void destroyFilmGenres(int filmId) {
        String sql = String.format(
                "DELETE FROM film_genre " +
                        "WHERE film_id = :%s",
                FilmGenreCol.FILM_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(FilmGenreCol.FILM_ID, filmId);

        jdbcTemplate.update(sql, sqlParams);
    }

    private Set<Genre> selectFilmGenres(int filmId) {
        String sql = String.format(
                "SELECT g.genre_id, " +
                        "g.genre " +
                        "FROM film_genre fg " +
                        "LEFT JOIN genre g ON g.genre_id = fg.genre_id " +
                        "WHERE fg.film_id = :%s " +
                        "ORDER BY g.genre_id",
                FilmGenreCol.FILM_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(FilmGenreCol.FILM_ID, filmId);

        return jdbcTemplate.query(sql, sqlParams, this::extractGenres);
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

}
