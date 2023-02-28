package ru.yandex.practicum.filmorate.model;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
public class User {

    private int id;
    @NotNull(message = "Электронная почта пльзователя не может быть пустой.")
    @Email(message = "Введеный email не соответствует формату.", regexp = "^\\w+(?:\\.\\w+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;
    @NotBlank(message = "Логин пользователя не может быть пустым и содержать пробелы.")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения пользователя не может быть в будущем.")
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = (name == null || name.isBlank() ? login : name);
        this.birthday = birthday;
    }
}
