package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {

    private int id = -1;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String login;

    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;

}
