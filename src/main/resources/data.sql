--INSERT INTO _user (user_id, login, name, email, birthday)
--VALUES
--(1, 'mark.hamill', 'Mark Hamill', 'mark.hamill@mail.com', '1964-02-02'),
--(2, 'carrie.fisher', 'Carrie Fisher', 'carrie.fisher@mail.com', '1972-09-29'),
--(3, 'andrew.stanton', 'Andrew Stanton', 'andrew.stanton@mail.com', '1986-12-22'),
--(4, 'lee.unkrich', 'Lee Unkrich', 'lee.unkrich@mail.com', '1987-11-23'),
--(5, 'bob.peterson', 'Bob Peterson', 'bob.peterson@mail.com', '2000-02-17');
--
--ALTER TABLE _user ALTER COLUMN user_id RESTART WITH 6;

--INSERT INTO friend (user_id, friend_id, is_confirmed)
--VALUES
--(1, 2, false),
--(1, 3, true),
--(3, 1, true);

INSERT INTO mpa_rating (mpa_rating_id, mpa_rating, mpa_meaning)
VALUES
(1, 'G', 'General audiences'),
(2, 'PG', 'Parental guidance suggested'),
(3, 'PG-13', 'Parents strongly cautioned'),
(4, 'R', 'Restricted'),
(5, 'NC-17', 'Clearly adult');

ALTER TABLE mpa_rating ALTER COLUMN mpa_rating_id RESTART WITH 6;

--INSERT INTO film (film_id, name, description, release_date, duration, rating_id)
--VALUES
--(1, 'Four Rooms', 'It''s Ted the Bellhop''s first night on the job... and the hotel''s very unusual guests are about to place him in some outrageous predicaments. It seems that this evening''s room service is serving up one unbelievable happening after another.', '1995-12-09', 98, 4),
--(2, 'Star Wars', 'Princess Leia is captured and held hostage by the evil Imperial forces in their effort to take over the galactic Empire. Venturesome Luke Skywalker and dashing captain Han Solo team together with the loveable robot duo R2-D2 and C-3PO to rescue the beautiful princess and restore peace and justice in the Empire.', '1977-05-25', 121, 2),
--(3, 'Finding Nemo', 'Nemo, an adventurous young clownfish, is unexpectedly taken from his Great Barrier Reef home to a dentist''s office aquarium. It''s up to his worrisome father Marlin and a friendly but forgetful fish Dory to bring Nemo home -- meeting vegetarian sharks, surfer dude turtles, hypnotic jellyfish, hungry seagulls, and more along the way.', '2003-05-30', 100, 1),
--(4, 'Forrest Gump', 'A man with a low IQ has accomplished great things in his life and been present during significant historic events -- in each case, far exceeding what anyone imagined he could do. Yet, despite all the things he has attained, his one true love eludes him. ''Forrest Gump'' is the story of a man who rose above his challenges, and who proved that determination, courage, and love are more important than ability.', '1994-07-06', 142, 3),
--(5, 'American Beauty', 'Lester Burnham, a depressed suburban father in a mid-life crisis, decides to turn his hectic life around after developing an infatuation with his daughter''s attractive friend.', '1999-09-15', 122, 4);
--
--ALTER TABLE film ALTER COLUMN film_id RESTART WITH 6;

INSERT INTO genre (genre_id, genre)
VALUES
(1, 'Комедия'),
(2, 'Драма'),
(3, 'Мультфильм'),
(4, 'Триллер'),
(5, 'Документальный'),
(6, 'Боевик');

ALTER TABLE genre ALTER COLUMN genre_id RESTART WITH 7;

--INSERT INTO genre (genre_id, genre)
--VALUES
--(1, 'Action'),
--(2, 'Adventure'),
--(3, 'Comedy'),
--(4, 'Drama'),
--(5, 'Fantasy'),
--(6, 'Horror'),
--(7, 'Musical'),
--(8, 'Mystery'),
--(9, 'Romance'),
--(10, 'Science fiction'),
--(11, 'Sports'),
--(12, 'Thriller'),
--(13, 'Western');

--ALTER TABLE genre ALTER COLUMN genre_id RESTART WITH 14;

--INSERT INTO film_genre (film_id, genre_id)
--VALUES
--(1, 3),
--(2, 1),
--(2, 2),
--(2, 5),
--(2, 10),
--(3, 2),
--(3, 3),
--(4, 3),
--(4, 4),
--(4, 9),
--(5, 3),
--(5, 4),
--(5, 9);
--
--INSERT INTO _like (user_id, film_id)
--VALUES
--(1, 1),
--(1, 2),
--(1, 3),
--(1, 4),
--(1, 5),
--(2, 1),
--(3, 1);

COMMIT;