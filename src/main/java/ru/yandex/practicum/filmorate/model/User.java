package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {

    private int id;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z]+$")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;
}