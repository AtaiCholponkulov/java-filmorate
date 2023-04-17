package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.valid.Validator.validateFilm;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
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
        userService.get(userId);//check user existence
        film.addLike(userId);
        filmStorage.update(film);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.get(filmId);
        userService.get(userId);//check user existence
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
