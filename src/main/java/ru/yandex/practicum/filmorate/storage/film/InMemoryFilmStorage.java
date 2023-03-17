package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public Film add(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм id={}.", film.getId());
        return film;
    }

    @Override
    public Film get(int filmId) {
        return films.get(filmId);
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.info("Обновлен фильм id={}.", film.getId());
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }
}
