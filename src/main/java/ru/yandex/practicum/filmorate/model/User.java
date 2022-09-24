package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    @NotNull
    @Setter(AccessLevel.NONE)
    private Set<Integer> friendsIds = new HashSet<>();

    public boolean addFriend(int userId) {
        return friendsIds.add(userId);
    }

    public boolean removeFriend(int userId) {
        return friendsIds.remove(userId);
    }

}
