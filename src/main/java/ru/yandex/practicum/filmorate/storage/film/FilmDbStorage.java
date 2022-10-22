package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FailedToInsertFilmException;
import ru.yandex.practicum.filmorate.exception.FailedToUpdateFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmCol;
import ru.yandex.practicum.filmorate.storage.FilmGenreCol;
import ru.yandex.practicum.filmorate.storage.GenreCol;
import ru.yandex.practicum.filmorate.storage.LikeCol;
import ru.yandex.practicum.filmorate.storage.MpaRatingCol;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        log.info("Create film: {}", film);

        insertFilm(film);
        insertGenres(film.getId(), film.getGenres());
        insertLikes(film.getId(), film.getUserIdsLikes());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Update film: {}", film);

        _updateFilm(film);
        updateGenres(film);
        updateLikes(film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        log.info("Get films: ");

        var films = selectFilms();
        for (Film film : films) {
            var likes = selectLikes(film.getId());
            film.setUserIdsLikes(likes);
            var genres = selectGenres(film.getId());
            film.setGenres(genres);
        }
        return films;
    }

    @Override
    public Optional<Film> getFilm(int filmId) {
        log.info("Get film by id: {}", filmId);

        var film = selectFilm(filmId);
        if (film == null) {
            return Optional.empty();
        } else {
            var likes = selectLikes(filmId);
            film.setUserIdsLikes(likes);
            var genres = selectGenres(film.getId());
            film.setGenres(genres);
            return Optional.of(film);
        }
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

        int rowsAffected = namedJdbcTemplate.update(sql, sqlParams, keyHolder);
        if (rowsAffected == 0 || keyHolder.getKey() == null) {
            throw new FailedToInsertFilmException(film);
        }

        film.setId(keyHolder.getKey().intValue());
    }

    private void _updateFilm(Film film) throws FailedToUpdateFilmException {
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

        int rowsAffected = namedJdbcTemplate.update(sql, sqlParams);
        if (rowsAffected == 0) {
            throw new FailedToUpdateFilmException(film);
        }
    }

    private void updateGenres(Film film) {
        deleteGenres(film.getId());
        insertGenres(film.getId(), film.getGenres());
    }

    private void deleteGenres(int filmId) {
        String sql = String.format(
                "DELETE FROM film_genre " +
                        "WHERE film_id = :%s",
                FilmGenreCol.FILM_ID);

        var sqlParams = new MapSqlParameterSource(
                FilmGenreCol.FILM_ID, filmId);

        namedJdbcTemplate.update(sql, sqlParams);
    }

    private void insertGenres(int filmId, Set<Genre> genres) {
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

        namedJdbcTemplate.batchUpdate(sql, sqlParams);
    }

    private Set<Genre> selectGenres(int filmId) {
        String sql = String.format(
                "SELECT fg.genre_id, " +
                        "g.genre " +
                        "FROM film_genre fg " +
                        "LEFT JOIN genre g ON g.genre_id = fg.genre_id " +
                        "WHERE fg.film_id = :%s " +
                        "ORDER BY fg.genre_id",
                FilmGenreCol.FILM_ID);

        var sqlParams = new MapSqlParameterSource(
                FilmGenreCol.FILM_ID, filmId);

        var genreIds = namedJdbcTemplate.query(sql, sqlParams, this::mapGenre);
        return new HashSet<>(genreIds);
    }


    private void updateLikes(Film film) {
        deleteLikes(film.getId());
        insertLikes(film.getId(), film.getUserIdsLikes());
    }

    private void deleteLikes(int filmId) {
        String sql = String.format(
                "DELETE FROM _like " +
                        "WHERE film_id = :%s",
                LikeCol.FILM_ID);

        var sqlParams = new MapSqlParameterSource(
                LikeCol.FILM_ID, filmId);

        namedJdbcTemplate.update(sql, sqlParams);
    }

    private void insertLikes(int filmId, Set<Integer> userIdsLikes) {
        String sql = String.format(
                "INSERT INTO _like (user_id, film_id) " +
                        "VALUES (:%s, :%s)",
                LikeCol.USER_ID,
                LikeCol.FILM_ID);

        var sqlParams = userIdsLikes.stream()
                .map(userId -> new MapSqlParameterSource()
                        .addValue(LikeCol.USER_ID, userId)
                        .addValue(LikeCol.FILM_ID, filmId))
                .toArray(SqlParameterSource[]::new);

        namedJdbcTemplate.batchUpdate(sql, sqlParams);
    }

    private Set<Integer> selectLikes(int filmId) {
        String sql = String.format(
                "SELECT l.user_id " +
                        "FROM _like l " +
                        "WHERE l.film_id = :%s",
                LikeCol.FILM_ID);

        var sqlParams = new MapSqlParameterSource(
                LikeCol.FILM_ID, filmId);

        var userIdsLikes = namedJdbcTemplate.queryForList(sql, sqlParams, Integer.class);
        return new HashSet<>(userIdsLikes);
    }

    private List<Film> selectFilms() {
        String sql = "SELECT f.film_id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_rating_id, " +
                "mr.mpa_rating, " +
                "f.rank " +
                "FROM film f " +
                "LEFT JOIN mpa_rating mr ON mr.mpa_rating_id = f.mpa_rating_id";

        return namedJdbcTemplate.query(sql, this::mapFilm);
    }

    private Film selectFilm(int filmId) {
        String sql = String.format(
                "SELECT f.film_id, " +
                        "f.name, " +
                        "f.description, " +
                        "f.release_date, " +
                        "f.duration, " +
                        "mr.mpa_rating_id, " +
                        "mr.mpa_rating, " +
                        "f.rank " +
                        "FROM FILM f " +
                        "LEFT JOIN mpa_rating mr ON mr.mpa_rating_id = f.mpa_rating_id " +
                        "WHERE f.film_id = :%s",
                FilmCol.FILM_ID);

        var sqlParams = new MapSqlParameterSource()
                .addValue(FilmCol.FILM_ID, filmId);

        var films = namedJdbcTemplate.query(sql, sqlParams, this::mapFilm);
        return films.size() == 1
                ? films.get(0)
                : null;
    }

    private Film mapFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt(FilmCol.FILM_ID));
        film.setName(rs.getString(FilmCol.NAME));
        film.setDescription(rs.getString(FilmCol.DESCRIPTION));
        film.setReleaseDate(rs.getDate(FilmCol.RELEASE_DATE).toLocalDate());
        film.setDuration(rs.getInt(FilmCol.DURATION));
        film.setRank(rs.getInt(FilmCol.RANK));

        MpaRating r = new MpaRating();
        r.setId(rs.getInt(MpaRatingCol.MPA_RATING_ID));
        r.setRating(rs.getString(MpaRatingCol.MPA_RATING));
        film.setMpaRating(r);
        return film;
    }

    private Genre mapGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt(GenreCol.GENRE_ID));
        genre.setGenre(rs.getString(GenreCol.GENRE));
        return genre;
    }

}
