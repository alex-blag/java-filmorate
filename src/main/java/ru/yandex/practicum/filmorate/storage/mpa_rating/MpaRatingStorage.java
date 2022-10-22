package ru.yandex.practicum.filmorate.storage.mpa_rating;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;
import java.util.Optional;

public interface MpaRatingStorage {

    List<MpaRating> getMpaRatings();

    Optional<MpaRating> getMpaRating(int mpaRatingId);

}
