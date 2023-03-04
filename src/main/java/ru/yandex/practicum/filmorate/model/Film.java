package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private int id;
    @NotBlank(message = "Название фильма было пустым.")
    private String name;
    @Size(max = 200, message = "Описание фильма превысило 200 символов.")
    private String description;
    @NotNull(message = "Дата релиза фильма не может быть пустой.")
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма должна быть положительной.")
    private int duration;
}
