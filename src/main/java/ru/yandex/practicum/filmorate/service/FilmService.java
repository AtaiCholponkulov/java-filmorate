package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film get(int filmId) {
        return filmStorage.get(filmId);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(int filmId, int userId) {
        if (!filmStorage.has(filmId))
            throw new NotFoundException("Такого фильма нет в базе id=" + filmId);
        if (!userStorage.has(userId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);
        filmStorage.get(filmId).addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        if (!filmStorage.has(filmId))
            throw new NotFoundException("Такого фильма нет в базе id=" + filmId);
        if (!userStorage.has(userId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);
        filmStorage.get(filmId).removeLike(userId);
    }

    public Collection<Film> getPopular(int count) {
        if (count < 1) throw new ValidationException("count не может быть меньше 1.");

        Collection<Film> films = filmStorage.getAll();
        int lengthOfReturn = Math.min(Math.min(count, 10), films.size());

        return films.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())  //descending order
                .collect(Collectors.toList())
                .subList(0, lengthOfReturn);
    }
}
