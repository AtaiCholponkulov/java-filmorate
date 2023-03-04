package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FilmService {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, Month.DECEMBER, 28);


    public Film add(Film film) {
        validateReleaseDate(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм id={}.", film.getId());
        return film;
    }

    public Film update(Film film) {
        validateReleaseDate(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Такого фильма не в базе.");
            throw new ValidationException("Такого фильма нет в базе.");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм id={}.", film.getId());
        return film;
    }

    public Collection<Film> getAll() {
        return films.values();
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            log.warn("Дата релиза фильма — не может быть раньше 28 декабря 1895 года.");
            throw new IllegalArgumentException("Дата релиза фильма — не может быть раньше 28 декабря 1895 года.");
        }
    }

}
