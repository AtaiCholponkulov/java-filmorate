package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class FilmService {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    public Film add(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм id={}.", film.getId());
        return film;
    }

    public Film update(Film film) {
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
}
