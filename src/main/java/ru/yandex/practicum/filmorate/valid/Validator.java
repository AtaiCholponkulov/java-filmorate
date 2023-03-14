package ru.yandex.practicum.filmorate.valid;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

public class Validator {

    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    public static void validateFilm(Film film) {
        if (film == null)
            throw new ValidationException("Тело запроса пустое.");
        if (film.getName() == null || film.getName().isBlank())
            throw new ValidationException("Название фильма пустое.");
        if (film.getDescription() != null && film.getDescription().length() > 200)
            throw new ValidationException("Описание фильма превысило 200 символов.");
        if (film.getReleaseDate() == null)
            throw new ValidationException("Дата релиза фильма не может быть пустой.");
        if (film.getReleaseDate().isBefore(EARLIEST_DATE))
            throw new ValidationException("Дата релиза фильма — не может быть раньше 28 декабря 1895 года.");
        if (film.getDuration() < 0)
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
    }

    public static void validateUser(User user) {
        if (user.getEmail() == null)
            throw new ValidationException("Электронная почта пльзователя не может быть пустой.");
        if (!user.getEmail().matches("^\\w+(?:\\.\\w+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"))
            throw new ValidationException("Введеный email не соответствует формату.");
        if (user.getLogin() == null || user.getLogin().isBlank())
            throw new ValidationException("Логин пользователя не может быть пустым или содержать пробелы.");
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения пользователя не может быть в будущем.");
    }
}
