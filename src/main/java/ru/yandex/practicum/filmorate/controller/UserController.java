package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        return userService.add(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userService.update(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }
}
