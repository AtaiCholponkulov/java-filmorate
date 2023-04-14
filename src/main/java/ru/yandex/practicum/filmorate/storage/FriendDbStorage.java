package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class FriendDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addUserFriends(User user) {
        String addUserFriendRelationSQLQuery = "INSERT INTO user_friend(user_id, friend_id) VALUES (?, ?)";
        Optional.ofNullable(user.getFriends()).ifPresent(friends -> {//check if set of friends is not null
            friends.forEach(friendId -> jdbcTemplate.update(addUserFriendRelationSQLQuery, user.getId(), friendId));
            log.info("Добавлены друзья пользователю id={}.", user.getId());
        });
    }

    public void updateUserFriends(User user) {
        jdbcTemplate.update("DELETE FROM user_friend WHERE user_id = " + user.getId());
        log.info("Удалены друзья пользователю id={}.", user.getId());

        this.addUserFriends(user);
    }

    public Set<Integer> getUserFriends(int userId) {
        Set<Integer> userFriends = new HashSet<>();
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("SELECT friend_id " +
                                                                "FROM user_friend " +
                                                                "WHERE user_id = ?;", userId);
        while (friendsRows.next()) {
            userFriends.add(friendsRows.getInt("friend_id"));
        }
        return userFriends;
    }
}
