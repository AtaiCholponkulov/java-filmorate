package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendDbStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Component("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FriendDbStorage friendStorage;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, FriendDbStorage friendStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.friendStorage = friendStorage;
    }

    @Override
    public User add(User user) {
        validateUserFriends(user);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());//adds record in user table, returns record id
        friendStorage.addUserFriends(user);

        log.info("Добавлен новый пользователь id={}.", user.getId());
        return user;
    }

    @Override
    public User get(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", id);

        if (!userRows.next()) throw new NotFoundException("Такого пользователя нет в базе id=" + id);

        User user = new User();
        user.setId(userRows.getInt("id"));
        user.setEmail(userRows.getString("email"));
        user.setLogin(userRows.getString("login"));
        user.setName(userRows.getString("name"));
        user.setBirthday(Optional.ofNullable(userRows.getDate("birthday"))
                        .map(Date::toLocalDate).orElse(null));
        user.setFriends(friendStorage.getUserFriends(id));

        return user;
    }

    @Override
    public User update(User user) {
        this.get(user.getId());//check user existence
        validateUserFriends(user);

        String updateUserSQLQuery = "UPDATE users " +
                                    "SET email = ?, login = ?, name = ?, birthday = ? " +
                                    "WHERE id = ?";
        jdbcTemplate.update(updateUserSQLQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        friendStorage.updateUserFriends(user);

        log.info("Обновлен пользователь id={}.", user.getId());
        return user;
    }

    @Override
    public Collection<User> getAll() {
        Collection<User> users = new ArrayList<>();

        SqlRowSet usersRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");

        while (usersRows.next()) {
            User user = new User();
            user.setId(usersRows.getInt("id"));
            user.setEmail(usersRows.getString("email"));
            user.setLogin(usersRows.getString("login"));
            user.setName(usersRows.getString("name"));
            user.setBirthday(Optional.ofNullable(usersRows.getDate("birthday"))
                            .map(Date::toLocalDate).orElse(null));
            user.setFriends(friendStorage.getUserFriends(usersRows.getInt("id")));

            users.add(user);
        }
        return users;
    }

    private void validateUserFriends(User user) {
        Optional.ofNullable(user.getFriends()).ifPresent(friends -> friends.forEach(this::get));
    }
}
