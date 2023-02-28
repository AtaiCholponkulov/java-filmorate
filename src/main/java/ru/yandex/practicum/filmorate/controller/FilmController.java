package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    private FilmService filmService;

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody @Valid Film film) {
        Film added = filmService.add(film);
        return new ResponseEntity<>(added, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody @Valid Film film) {
        return new ResponseEntity<>(filmService.update(film), HttpStatus.valueOf(200));
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }
}
