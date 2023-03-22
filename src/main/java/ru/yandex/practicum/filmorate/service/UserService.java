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

import static ru.yandex.practicum.filmorate.valid.Validator.validateUser;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        validateUser(user);
        return userStorage.add(user);
    }

    public User get(int userId) {
        User user = userStorage.get(userId);
        if (user == null)
            throw new NotFoundException("Такого пользователя нет в базе id=" + userId);
        return user;
    }

    public User update(User user) {
        validateUser(user);
        this.get(user.getId());
        return userStorage.update(user);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public void makeFriends(int userId, int friendId) {
        User user = this.get(userId);
        User friend = this.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void unFriend(int userId, int friendId) {
        User user = this.get(userId);
        User friend = this.get(friendId);
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public Collection<User> getMutual(int userId, int otherId) {
        User user = this.get(userId);
        User otherUser = this.get(otherId);

        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherUserFriends = otherUser.getFriends();

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    public Collection<User> getFriends(int userId) {
        User user = this.get(userId);

        return user.getFriends().stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }
}
