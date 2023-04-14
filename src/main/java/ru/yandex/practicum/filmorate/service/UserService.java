package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.valid.Validator.validateUser;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        validateUser(user);
        return userStorage.add(user);
    }

    public User get(int userId) {
        return userStorage.get(userId);
    }

    public User update(User user) {
        validateUser(user);
        return userStorage.update(user);
    }

    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    public void makeFriends(int userId, int friendId) {
        User user = userStorage.get(userId);
        userStorage.get(friendId);//check almost-friend existence
        user.addFriend(friendId);
        userStorage.update(user);
    }

    public void unFriend(int userId, int friendId) {
        User user = userStorage.get(userId);
        userStorage.get(friendId);//check almost-ex-friend existence
        user.removeFriend(friendId);
        userStorage.update(user);
    }

    public Collection<User> getMutual(int userId, int otherId) {
        User user = userStorage.get(userId);
        User otherUser = userStorage.get(otherId);

        Set<Integer> userFriends = user.getFriends();
        Set<Integer> otherUserFriends = otherUser.getFriends();

        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    public Collection<User> getFriends(int userId) {
        User user = userStorage.get(userId);

        return user.getFriends().stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }
}
