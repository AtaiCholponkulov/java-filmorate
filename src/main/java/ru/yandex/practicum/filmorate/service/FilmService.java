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

import static ru.yandex.practicum.filmorate.valid.Validator.validateFilm;

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
        validateFilm(film);
        return filmStorage.add(film);
    }

    public Film get(int filmId) {
        Film film = filmStorage.get(filmId);
        if (film == null)
            throw new NotFoundException("Такого фильма нет в базе id=" + filmId);
        return film;
    }

    public Film update(Film film) {
        validateFilm(film);
        this.get(film.getId());
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(int filmId, int userId) {
        Film film = this.get(filmId);
        if (userStorage.get(userId) == null)
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);
        film.addLike(userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = this.get(filmId);
        if (userStorage.get(userId) == null)
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);
        film.removeLike(userId);
    }

    public Collection<Film> getPopular(int count) {
        if (count < 1) throw new ValidationException("count не может быть меньше 1.");

        Collection<Film> films = filmStorage.getAll();

        return films.stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())  //descending order
                .limit(count)
                .collect(Collectors.toList());
    }
}
