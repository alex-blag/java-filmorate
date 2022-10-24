INSERT INTO mpa_rating (mpa_rating_id, mpa_rating, mpa_meaning)
VALUES
(1, 'G', 'General audiences'),
(2, 'PG', 'Parental guidance suggested'),
(3, 'PG-13', 'Parents strongly cautioned'),
(4, 'R', 'Restricted'),
(5, 'NC-17', 'Clearly adult');

ALTER TABLE mpa_rating ALTER COLUMN mpa_rating_id RESTART WITH 6;

INSERT INTO genre (genre_id, genre)
VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

ALTER TABLE genre ALTER COLUMN genre_id RESTART WITH 7;

COMMIT;