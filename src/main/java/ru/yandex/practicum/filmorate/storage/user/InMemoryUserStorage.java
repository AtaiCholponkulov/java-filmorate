package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User add(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь id={}", user.getId());
        return user;
    }

    @Override
    public User get(int userId) {
        return users.get(userId);
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        log.info("Обновлен пользователь id={}", user.getId());
        return user;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }
}
