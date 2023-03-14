package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.valid.Validator.validateUser;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User add(User user) {
        validateUser(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь id={}", user.getId());
        return user;
    }

    @Override
    public User get(int userId) {
        if (!users.containsKey(userId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);
        return users.get(userId);
    }

    @Override
    public User update(User user) {
        validateUser(user);
        if (!has(user.getId()))
            throw new NotFoundException("Такого пользователя нет в базе id=" + user.getId());
        users.put(user.getId(), user);
        log.info("Обновлен пользователь id={}", user.getId());
        return user;
    }

    @Override
    public boolean has(int userId) {
        return users.containsKey(userId);
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}
