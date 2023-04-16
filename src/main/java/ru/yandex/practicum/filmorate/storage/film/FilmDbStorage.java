package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.util.*;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, UserDbStorage userStorage, GenreDbStorage genreStorage, MpaDbStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Film add(Film film) {
        validateFilmMpaAndGenresAndLikes(film);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("id");

        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());//adds record in film table, returns record id
        genreStorage.addFilmGenres(film);
        film.getLikes().forEach(userId -> jdbcTemplate.batchUpdate(String.format("INSERT INTO film_like(film_id, user_id) " +
                "VALUES (%d, %d)", film.getId(), userId)));

        log.info("Добавлен новый фильм id={}.", film.getId());
        return film;
    }

    @Override
    public Film get(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT f.*, mpa.name AS mpa_name " +
                                                             "FROM film AS f " +
                                                             "JOIN mpa ON f.mpa_id=mpa.id " +
                                                             "WHERE f.id = ?", id);

        if (!filmRows.next()) throw new NotFoundException("Такого фильма нет в базе id=" + id);

        Film film = sqlResponseToFilm(filmRows);
        film.setGenres(genreStorage.getFilmGenres(film.getId()));
        return film;
    }

    @Override
    public Film update(Film film) {
        this.get(film.getId());//check film existence
        validateFilmMpaAndGenresAndLikes(film);

        String updateFilmSQLQuery = "UPDATE film " +
                                    "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                                    "WHERE id = ?";
        jdbcTemplate.update(updateFilmSQLQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        genreStorage.updateFilmGenres(film);
        jdbcTemplate.update("DELETE FROM film_like WHERE film_id = " + film.getId());
        film.getLikes().forEach(userId -> jdbcTemplate.batchUpdate(
                String.format("INSERT INTO film_like(film_id, user_id) " +
                              "VALUES (%d, %d)", film.getId(), userId)));

        log.info("Обновлен фильм id={}.", film.getId());
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = new ArrayList<>();

        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("SELECT f.*, mpa.name AS mpa_name " +
                                                              "FROM film AS f " +
                                                              "JOIN mpa ON f.mpa_id=mpa.id");

        while (filmsRows.next()) {
            films.add(sqlResponseToFilm(filmsRows));
        }
        setGenresToAllFilms(films);

        return films;
    }

    private void validateFilmMpaAndGenresAndLikes(Film film) {
        mpaStorage.get(film.getMpa().getId());//validates film mpa

        film.getGenres().forEach(genre -> genreStorage.get(genre.getId()));//validates film genres

        film.getLikes().forEach(userStorage::get);//validates film likes of users
    }

    private Film sqlResponseToFilm(SqlRowSet filmRows) {
        Film film = new Film();
        film.setId(filmRows.getInt("id"));
        film.setName(filmRows.getString("name"));
        film.setDescription(filmRows.getString("description"));
        film.setReleaseDate(Optional.ofNullable(filmRows.getDate("release_date")).map(Date::toLocalDate).orElse(null));
        film.setDuration(filmRows.getInt("duration"));

        MPARating filmMpa = new MPARating();
        filmMpa.setId(filmRows.getInt("mpa_id"));
        filmMpa.setName(filmRows.getString("MPA_NAME"));
        film.setMpa(filmMpa);

        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM film_like WHERE film_id = ?", film.getId());
        while (likesRows.next()) {
            film.addLike(likesRows.getInt("user_id"));
        }

        return film;
    }

    private void setGenresToAllFilms(Collection<Film> films) {
        Map<Integer, Set<Genre>> filmGenresMap = genreStorage.getFilmsGenresMap();
        films.forEach(film -> film.setGenres(Optional.ofNullable(filmGenresMap.get(film.getId()))
                                                     .orElse(new HashSet<>())));
    }
}
