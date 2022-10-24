DROP TABLE IF EXISTS _user CASCADE;

DROP TABLE IF EXISTS friendship CASCADE;

DROP TABLE IF EXISTS film CASCADE;

DROP TABLE IF EXISTS mpa_rating CASCADE;

DROP TABLE IF EXISTS film_rank CASCADE;

DROP TABLE IF EXISTS film_genre CASCADE;

DROP TABLE IF EXISTS genre CASCADE;

DROP TABLE IF EXISTS _like CASCADE;

CREATE TABLE _user (
    user_id 	INTEGER NOT NULL AUTO_INCREMENT,
    login		CHARACTER VARYING NOT NULL,
    name 		CHARACTER VARYING,
    email 		CHARACTER VARYING,
    birthday	DATE,
	CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT uc_user_login UNIQUE (login)
);

CREATE TABLE friendship (
    user_id			INTEGER	NOT NULL,
    friend_id 		INTEGER NOT NULL,
    is_confirmed	BOOLEAN NOT NULL,
	CONSTRAINT pk_friendship PRIMARY KEY (user_id, friend_id)
);

CREATE TABLE film (
    film_id			INTEGER NOT NULL AUTO_INCREMENT,
    name			CHARACTER VARYING NOT NULL,
    description 	CHARACTER VARYING,
    release_date	DATE,
    duration		SMALLINT,
    mpa_rating_id	INTEGER,
    rank            INTEGER,
	CONSTRAINT pk_film PRIMARY KEY (film_id),
    CONSTRAINT uc_film_name UNIQUE (name)
);

CREATE TABLE mpa_rating (
	mpa_rating_id   INTEGER NOT NULL AUTO_INCREMENT,
    mpa_rating      CHARACTER VARYING NOT NULL,
    mpa_meaning	    CHARACTER VARYING NOT NULL,
	CONSTRAINT pk_mpa_rating PRIMARY KEY (mpa_rating_id),
	CONSTRAINT uc_mpa_rating_mpa_rating UNIQUE (mpa_rating)
);

CREATE TABLE film_genre (
    film_id		INTEGER NOT NULL,
    genre_id	INTEGER NOT NULL,
	CONSTRAINT pk_film_genre PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE genre (
    genre_id	INTEGER NOT NULL AUTO_INCREMENT,
    genre 		CHARACTER VARYING NOT NULL,
    CONSTRAINT pk_genre PRIMARY KEY (genre_id),
	CONSTRAINT uc_genre_genre UNIQUE (genre)
);

CREATE TABLE _like (
    film_id	INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT pk_like PRIMARY KEY (film_id, user_id)
);

ALTER TABLE friendship
ADD CONSTRAINT fk_friendship_user_id
FOREIGN KEY(user_id) REFERENCES _user (user_id);

ALTER TABLE friendship
ADD CONSTRAINT fk_friendship_friend_id
FOREIGN KEY(friend_id) REFERENCES _user (user_id);

ALTER TABLE film
ADD CONSTRAINT fk_film_mpa_rating
FOREIGN KEY(mpa_rating_id) REFERENCES mpa_rating (mpa_rating_id);

ALTER TABLE film_genre
ADD CONSTRAINT fk_film_genre_film_id
FOREIGN KEY(film_id) REFERENCES film (film_id);

ALTER TABLE film_genre
ADD CONSTRAINT fk_film_genre_genre_id
FOREIGN KEY(genre_id) REFERENCES genre (genre_id);

ALTER TABLE _like
ADD CONSTRAINT fk_like_film_id
FOREIGN KEY(film_id) REFERENCES film (film_id);

ALTER TABLE _like
ADD CONSTRAINT fk_like_user_id
FOREIGN KEY(user_id) REFERENCES _user (user_id);

COMMIT;