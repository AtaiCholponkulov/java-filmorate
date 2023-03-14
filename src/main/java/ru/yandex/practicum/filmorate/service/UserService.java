package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User get(int userId) {
        return userStorage.get(userId);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public void makeFriends(int userId, int friendId) {
        if (!userStorage.has(userId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);
        if (!userStorage.has(friendId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + friendId);
        userStorage.get(userId).addFriend(friendId);
        userStorage.get(friendId).addFriend(userId);
    }

    public void unFriend(int userId, int friendId) {
        if (!userStorage.has(userId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);
        if (!userStorage.has(friendId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + friendId);
        userStorage.get(userId).removeFriend(friendId);
        userStorage.get(friendId).removeFriend(userId);
    }

    public Collection<User> getMutual(int userId, int otherId) {
        if (!userStorage.has(userId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);
        if (!userStorage.has(otherId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + otherId);

        Set<Integer> userFriends = userStorage.get(userId).getFriends();
        Set<Integer> otherUserFriends = userStorage.get(otherId).getFriends();

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    public Collection<User> getFriends(int userId) {
        if (!userStorage.has(userId))
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);

        return userStorage.get(userId).getFriends().stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }
}
