package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable(name = "id") int userId) {
        return userService.get(userId);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.update(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeFriends(@PathVariable(name = "id") int userId, @PathVariable int friendId) {
        userService.makeFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void unFriend(@PathVariable(name = "id") int userId, @PathVariable int friendId) {
        userService.unFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getMutual(@PathVariable(name = "id") int userId, @PathVariable int otherId) {
        return userService.getMutual(userId, otherId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable(name = "id") int userId) {
        return userService.getFriends(userId);
    }
}
