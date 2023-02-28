package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Month;

@Getter
@Setter
public class Film {

    private int id;
    @NotBlank(message = "Название фильма было пустым.")
    private String name;
    @Size(max = 200, message = "Описание фильма превысило 200 символов.")
    private String description;
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма должна быть положительной.")
    private int duration;
    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        if (releaseDate.isBefore(EARLIEST_DATE))
            throw new IllegalArgumentException("Дата релиза фильма — не может быть раньше 28 декабря 1895 года.");
        else this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
