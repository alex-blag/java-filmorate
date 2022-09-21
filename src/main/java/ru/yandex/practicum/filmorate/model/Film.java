package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ru.yandex.practicum.filmorate.annotation.IsAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @IsAfter(date = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @NotNull
    @Setter(AccessLevel.NONE)
    private Set<Integer> userIdsThatLiked = new HashSet<>();

    public boolean addLike(int userId) {
        return userIdsThatLiked.add(userId);
    }

    public boolean removeLike(int userId) {
        return userIdsThatLiked.remove(userId);
    }

}
