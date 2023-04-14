package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MPARating get(int mpaId) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT name FROM mpa WHERE id = ?", mpaId);

        if (!mpaRows.next()) throw new NotFoundException("Такого mpa нет в базе id=" + mpaId);

        MPARating mpa = new MPARating();
        mpa.setId(mpaId);
        mpa.setName(mpaRows.getString("name"));
        return mpa;
    }

    public Collection<MPARating> getAll() {
        Collection<MPARating> mpaRatings = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa");
        while (mpaRows.next()) {
            MPARating mpa = new MPARating();
            mpa.setId(mpaRows.getInt("id"));
            mpa.setName(mpaRows.getString("name"));
            mpaRatings.add(mpa);
        }
        return mpaRatings;
    }
}
