package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FailedToInsertFilmException;
import ru.yandex.practicum.filmorate.exception.FailedToUpdateFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmCol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private static final String LIMIT = "limit";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        log.info("Create film: {}", film);
        insertFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Update film: {}", film);
        updateSetFilm(film);
        return film;
    }

    @Override
    public Optional<Film> readFilm(int filmId) {
        log.info("Read film: {}", filmId);
        var film = selectFilm(filmId);
        return Optional.ofNullable(film);
    }

    @Override
    public List<Film> readFilms() {
        log.info("Read films: ");
        return selectFilms();
    }

    @Override
    public List<Film> readPopularFilms(int numberOfFilms) {
        log.info("Read {} popular films: ", numberOfFilms);
        return selectPopularFilms(numberOfFilms);
    }

    private void insertFilm(Film film) throws FailedToInsertFilmException {
        String sql = String.format(
                "INSERT INTO film (name, description, release_date, duration, mpa_rating_id, rank) " +
                        "VALUES (:%s, :%s, :%s, :%s, :%s, :%s)",
                FilmCol.NAME,
                FilmCol.DESCRIPTION,
                FilmCol.RELEASE_DATE,
                FilmCol.DURATION,
                FilmCol.MPA_RATING_ID,
                FilmCol.RANK);

        var sqlParams = new MapSqlParameterSource()
                .addValue(FilmCol.NAME, film.getName())
                .addValue(FilmCol.DESCRIPTION, film.getDescription())
                .addValue(FilmCol.RELEASE_DATE, film.getReleaseDate())
                .addValue(FilmCol.DURATION, film.getDuration())
                .addValue(FilmCol.MPA_RATING_ID, film.getMpaRating().getId())
                .addValue(FilmCol.RANK, film.getRank());

        var keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(sql, sqlParams, keyHolder);
        if (rowsAffected == 0 || keyHolder.getKey() == null) {
            throw new FailedToInsertFilmException(film);
        }

        film.setId(keyHolder.getKey().intValue());
    }

    private void updateSetFilm(Film film) throws FailedToUpdateFilmException {
        String sql = String.format(
                "UPDATE film f " +
                        "SET f.name = :%s, " +
                        "f.description = :%s, " +
                        "f.release_date = :%s, " +
                        "f.duration = :%s, " +
                        "f.mpa_rating_id = :%s, " +
                        "f.rank = :%s " +
                        "WHERE f.film_id = :%s",
                FilmCol.NAME,
                FilmCol.DESCRIPTION,
                FilmCol.RELEASE_DATE,
                FilmCol.DURATION,
                FilmCol.MPA_RATING_ID,
                FilmCol.RANK,
                FilmCol.FILM_ID
        );

        var sqlParams = new MapSqlParameterSource()
                .addValue(FilmCol.NAME, film.getName())
                .addValue(FilmCol.DESCRIPTION, film.getDescription())
                .addValue(FilmCol.RELEASE_DATE, film.getReleaseDate())
                .addValue(FilmCol.DURATION, film.getDuration())
                .addValue(FilmCol.MPA_RATING_ID, film.getMpaRating().getId())
                .addValue(FilmCol.RANK, film.getRank())
                .addValue(FilmCol.FILM_ID, film.getId());

        int rowsAffected = jdbcTemplate.update(sql, sqlParams);
        if (rowsAffected == 0) {
            throw new FailedToUpdateFilmException(film);
        }
    }

    private Film selectFilm(int filmId) {
        String sql = String.format(
                "SELECT * " +
                        "FROM film f " +
                        "WHERE f.film_id = :%s",
                FilmCol.FILM_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(FilmCol.FILM_ID, filmId);

        var films = jdbcTemplate.query(sql, sqlParams, this::mapFilm);
        return films.size() == 1
                ? films.get(0)
                : null;
    }

    private List<Film> selectFilms() {
        String sql =
                "SELECT * " +
                        "FROM film f ";

        return jdbcTemplate.query(sql, this::mapFilm);
    }

    private List<Film> selectPopularFilms(int numberOfFilms) {
        String sql = String.format(
                "SELECT * " +
                        "FROM film f " +
                        "ORDER BY f.rank, f.film_id DESC " +
                        "LIMIT :%s",
                LIMIT);

        var sqlParams = new MapSqlParameterSource()
                .addValue(LIMIT, numberOfFilms);

        return jdbcTemplate.query(sql, sqlParams, this::mapFilm);
    }

    private Film mapFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt(FilmCol.FILM_ID));
        film.setName(rs.getString(FilmCol.NAME));
        film.setDescription(rs.getString(FilmCol.DESCRIPTION));
        film.setReleaseDate(rs.getDate(FilmCol.RELEASE_DATE).toLocalDate());
        film.setDuration(rs.getInt(FilmCol.DURATION));

        MpaRating mr = new MpaRating();
        mr.setId(rs.getInt(FilmCol.MPA_RATING_ID));
        film.setMpaRating(mr);

        film.setRank(rs.getInt(FilmCol.RANK));
        return film;
    }

}
