package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage{
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
        film.getLikes().forEach(userId -> jdbcTemplate.update("INSERT INTO film_like(film_id, user_id) " +
                                                                  "VALUES (?, ?)", film.getId(), userId));

        log.info("Добавлен новый фильм id={}.", film.getId());
        return film;
    }

    @Override
    public Film get(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE id = ?", id);

        if (!filmRows.next()) throw new NotFoundException("Такого фильма нет в базе id=" + id);

        Film film = new Film();
        film.setId(filmRows.getInt("id"));
        film.setName(filmRows.getString("name"));
        film.setDescription(filmRows.getString("description"));
        film.setReleaseDate(Optional.ofNullable(filmRows.getDate("release_date")).map(Date::toLocalDate).orElse(null));
        film.setDuration(filmRows.getInt("duration"));
        film.setMpa(mpaStorage.get(filmRows.getInt("mpa_id")));
        film.setGenres(genreStorage.getFilmGenres(id));

        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM film_like WHERE film_id = ?", id);
        while (likesRows.next()) {
            film.addLike(likesRows.getInt("user_id"));
        }

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
        film.getLikes().forEach(userId -> jdbcTemplate.update("INSERT INTO film_like(film_id, user_id) " +
                                                                  "VALUES (?, ?)", film.getId(), userId));

        log.info("Обновлен фильм id={}.", film.getId());
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        Collection<Film> films = new ArrayList<>();

        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("SELECT * FROM film");

        while (filmsRows.next()) {
            Film film = new Film();
            film.setId(filmsRows.getInt("id"));
            film.setName(filmsRows.getString("name"));
            film.setDescription(filmsRows.getString("description"));
            film.setReleaseDate(Optional.ofNullable(filmsRows.getDate("release_date")).map(Date::toLocalDate).orElse(null));
            film.setDuration(filmsRows.getInt("duration"));
            film.setMpa(mpaStorage.get(filmsRows.getInt("mpa_id")));
            film.setGenres(genreStorage.getFilmGenres(film.getId()));

            SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM film_like WHERE film_id = ?", film.getId());
            while (likesRows.next()) {
                film.addLike(likesRows.getInt("user_id"));
            }

            films.add(film);
        }
        return films;
    }

    private void validateFilmMpaAndGenresAndLikes(Film film) {
        mpaStorage.get(film.getMpa().getId());//validates film mpa

        film.getGenres().forEach(genre -> genreStorage.get(genre.getId()));//validates film genres

        film.getLikes().forEach(userStorage::get);//validates film likes of users
    }
}
