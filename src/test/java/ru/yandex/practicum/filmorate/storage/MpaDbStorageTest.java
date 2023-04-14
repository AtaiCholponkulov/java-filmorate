package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {

    private final MpaDbStorage mpaStorage;
    private final MPARating g = new MPARating();
    private final MPARating pg = new MPARating();
    private final MPARating pg13 = new MPARating();
    private final MPARating r = new MPARating();
    private final MPARating nc17 = new MPARating();

    @BeforeEach
    public void beforeEach() {
        g.setId(1);
        pg.setId(2);
        pg13.setId(3);
        r.setId(4);
        nc17.setId(5);
    }

    @Test
    void get() {
        assertEquals(g, mpaStorage.get(1));
        assertEquals(pg, mpaStorage.get(2));
        assertEquals(pg13, mpaStorage.get(3));
        assertEquals(r, mpaStorage.get(4));
        assertEquals(nc17, mpaStorage.get(5));
    }

    @Test
    void getAll() {
        assertTrue(mpaStorage.getAll().contains(g));
        assertTrue(mpaStorage.getAll().contains(pg));
        assertTrue(mpaStorage.getAll().contains(pg13));
        assertTrue(mpaStorage.getAll().contains(r));
        assertTrue(mpaStorage.getAll().contains(nc17));
    }
}
