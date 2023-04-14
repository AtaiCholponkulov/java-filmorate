package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {

    private final GenreDbStorage genreStorage;
    private final FilmDbStorage filmStorage;
    private Film film = new Film();
    private final Genre comedy = new Genre();
    private final Genre drama = new Genre();

    @BeforeEach
    public void beforeEach() {
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(100);
        MPARating mpa = new MPARating();
        mpa.setId(1);
        film.setMpa(mpa);

        comedy.setId(1);
        drama.setId(2);
    }

    @Test
    void genreDaoShouldReturnComedy() {
        assertEquals("Комедия", genreStorage.get(1).getName());
    }

    @Test
    void genreDaoShouldReturnAllGenres() {
        Collection<Genre> genres = genreStorage.getAll();
        List<String> genreNames = List.of("Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик");
        genres.stream()
                .map(Genre::getName)
                .forEach(genreName -> assertTrue(genreNames.contains(genreName)));
    }

    @Test
    void genreDaoShouldAddFilmGenres() {
        film.setGenres(new HashSet<>(List.of(comedy, drama)));
        film = filmStorage.add(film);
        Set<Genre> filmGenres = filmStorage.get(film.getId()).getGenres();
        assertEquals(2, filmGenres.size());
        assertTrue(filmGenres.contains(comedy));
        assertTrue(filmGenres.contains(drama));
    }

    @Test
    void genreDaoShouldUpdateFilmGenres() {
        film = filmStorage.add(film);
        Set<Genre> filmGenres = filmStorage.get(film.getId()).getGenres();
        assertEquals(0, filmGenres.size());

        film.setGenres(new HashSet<>(List.of(comedy, drama)));
        film = filmStorage.update(film);
        Set<Genre> updatedFilmGenres = filmStorage.get(film.getId()).getGenres();
        assertEquals(2, updatedFilmGenres.size());
        assertTrue(updatedFilmGenres.contains(comedy));
        assertTrue(updatedFilmGenres.contains(drama));
    }

    @Test
    void genreDaoShouldGetFilmGenres() {
        film = filmStorage.add(film);
        Set<Genre> filmGenres = filmStorage.get(film.getId()).getGenres();
        assertEquals(0, filmGenres.size());
    }
}
