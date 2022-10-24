package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
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

    private int id = -1;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @IsAfter(date = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @NotNull
    @JsonProperty("mpa")
    private MpaRating mpaRating;

    @NotNull
    private Set<Genre> genres = new HashSet<>();

    @JsonProperty("rate")
    private int rank;

}
