package ru.yandex.practicum.filmorate.model;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
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
}
