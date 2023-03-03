package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    public User add(User user) {
        validateName(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь id={}", user.getId());
        return user;
    }

    public User update(User user) {
        validateName(user);
        if (!users.containsKey(user.getId())) {
            log.warn("Такого пользователя нет в базе.");
            throw new ValidationException("Такого пользователя нет в базе.");
        }
        users.put(user.getId(), user);
        log.info("Обновлен пользователь id={}", user.getId());
        return user;
    }

    public Collection<User> getAll() {
        return users.values();
    }

    private void validateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }
}
