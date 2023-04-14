package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.Objects;

@Data
public class MPARating {
    private Integer id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MPARating mpaRating = (MPARating) o;
        return Objects.equals(getId(), mpaRating.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
