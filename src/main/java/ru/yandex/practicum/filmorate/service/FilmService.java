package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.valid.Validator.validateFilm;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) {
        validateFilm(film);
        return filmStorage.add(film);
    }

    public Film get(int filmId) {
        return filmStorage.get(filmId);
    }

    public Film update(Film film) {
        validateFilm(film);
        return filmStorage.update(film);
    }

    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.get(filmId);
        userStorage.get(userId);//check user existence
        film.addLike(userId);
        filmStorage.update(film);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.get(filmId);
        userStorage.get(userId);//check user existence
        film.removeLike(userId);
        filmStorage.update(film);
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
