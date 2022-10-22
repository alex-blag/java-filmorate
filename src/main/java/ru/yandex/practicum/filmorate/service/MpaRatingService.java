package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaRatingIdException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.mpa_rating.MpaRatingDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaRatingService {

    @Qualifier("mpaRatingDbStorage")
    private final MpaRatingDbStorage mpaRatingDbStorage;

    public List<MpaRating> getMpaRatings() {
        return mpaRatingDbStorage.getMpaRatings();
    }

    public MpaRating getMpaRating(int mpaRatingId) {
        return mpaRatingDbStorage
                .getMpaRating(mpaRatingId)
                .orElseThrow(() -> new NoSuchMpaRatingIdException(mpaRatingId));
    }

}
