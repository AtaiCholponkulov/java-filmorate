package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;
    private Film film1 = new Film();

    @BeforeEach
    public void beforeEach() {
        film1.setName("nisi eiusmod");
        film1.setDescription("adipisicing");
        film1.setReleaseDate(LocalDate.of(1967, 3, 25));
        film1.setDuration(100);
        MPARating mpa = new MPARating();
        mpa.setId(1);
        film1.setMpa(mpa);
    }

    @Test
    public void filmDaoShouldAddFilm() {
        assertEquals(0, film1.getId());
        film1 = filmStorage.add(film1);
        assertEquals(filmStorage.getAll().size(), film1.getId());
    }

    @Test
    public void filmDaoShouldReturnFilm() {
        film1 = filmStorage.add(film1);
        Film dbFilm = filmStorage.get(film1.getId());

        assertEquals(film1.getId(), dbFilm.getId());
        assertEquals(film1.getName(), dbFilm.getName());
        assertEquals(film1.getDescription(), dbFilm.getDescription());
        assertEquals(film1.getReleaseDate(), dbFilm.getReleaseDate());
        assertEquals(film1.getDuration(), dbFilm.getDuration());
        assertEquals(film1.getMpa(), dbFilm.getMpa());
    }

    @Test
    public void filmDaoShouldUpdateFilm() {
        film1 = filmStorage.add(film1);

        Film film1Updated = new Film();
        film1Updated.setId(film1.getId());
        film1Updated.setName("Film Updated");
        film1Updated.setReleaseDate(LocalDate.of(1989, 4, 17));
        film1Updated.setDescription("New film update decription");
        film1Updated.setDuration(190);
        MPARating mpa2 = new MPARating();
        mpa2.setId(2);
        film1Updated.setMpa(mpa2);

        filmStorage.update(film1Updated);
        Film dbFilm1Updated = filmStorage.get(film1.getId());

        assertEquals(film1Updated.getId(), dbFilm1Updated.getId());
        assertEquals(film1Updated.getName(), dbFilm1Updated.getName());
        assertEquals(film1Updated.getDescription(), dbFilm1Updated.getDescription());
        assertEquals(film1Updated.getReleaseDate(), dbFilm1Updated.getReleaseDate());
        assertEquals(film1Updated.getDuration(), dbFilm1Updated.getDuration());
        assertEquals(film1Updated.getMpa(), dbFilm1Updated.getMpa());
    }

    @Test
    public void filmDaoShouldReturnAll() {
        int initialSize = filmStorage.getAll().size();
        filmStorage.add(film1);
        assertEquals(initialSize + 1, filmStorage.getAll().size());
    }
}