package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre get(int genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT name FROM genre WHERE id = ?", genreId);
        if (!genreRows.next()) throw new NotFoundException("Такого жанра нет в базе id=" + genreId);
        Genre genre = new Genre();
        genre.setId(genreId);
        genre.setName(genreRows.getString("name"));
        return genre;
    }

    public Collection<Genre> getAll() {
        Collection<Genre> genres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genre");
        while (genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("id"));
            genre.setName(genreRows.getString("name"));
            genres.add(genre);
        }
        return genres;
    }

    public void addFilmGenres(Film film) {
        String addFilmGenreRelationSQLQuery = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";
        film.getGenres()
                .forEach(genre -> jdbcTemplate.update(addFilmGenreRelationSQLQuery, film.getId(), genre.getId()));
        log.info("Добавлены жанры фильму id={}.", film.getId());
    }

    public void updateFilmGenres(Film film) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = " + film.getId());
        log.info("Удалены жанры фильму id={}.", film.getId());

        this.addFilmGenres(film);
    }

    public Set<Genre> getFilmGenres(int filmId) {
        Set<Genre> filmGenres = new HashSet<>();
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("SELECT g.id, g.name " +
                                                                "FROM film_genre AS fg " +
                                                                "JOIN genre AS g ON fg.genre_id=g.id " +
                                                                "WHERE fg.film_id = ?;", filmId);
        while (genresRows.next()) {
            Genre genre = new Genre();
            genre.setId(genresRows.getInt("id"));
            genre.setName(genresRows.getString("name"));
            filmGenres.add(genre);
        }
        return filmGenres;
    }
}
