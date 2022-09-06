package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.IsAfter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private int id;

    @NotEmpty
    private String name;

    @Size(max = 200)
    private String description;

    @IsAfter(date = "1895-12-28")
    private LocalDate releaseDate;

    @Positive
    private int duration;
}
