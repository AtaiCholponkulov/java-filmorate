package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendDbStorageTest {

    private final UserDbStorage userStorage;
    private final FriendDbStorage friendStorage;
    private User user1 = new User();
    private User user2 = new User();

    @BeforeEach
    public void beforeEach() {
        user1.setLogin("dolore");
        user1.setName("Nick Name");
        user1.setEmail("mail@mail.ru");
        user1.setBirthday(LocalDate.of(1946, 8, 20));

        user2.setLogin("friend");
        user2.setName("friend adipisicing");
        user2.setEmail("friend@mail.ru");
        user2.setBirthday(LocalDate.of(1976, 8, 20));
    }

    @Test
    public void friendDaoShouldAddFriendUser2ToUser1() {
        user2 = userStorage.add(user2);
        user1.addFriend(user2.getId());
        user1 = userStorage.add(user1);
        assertTrue(friendStorage.getUserFriends(user1.getId()).contains(user2.getId()));
    }

    @Test
    public void friendDaoShouldUpdateUser1Friends() {
        user1 = userStorage.add(user1);
        user2 = userStorage.add(user2);
        assertEquals(0, user1.getFriends().size());
        user1.addFriend(user2.getId());
        userStorage.update(user1);
        assertEquals(1, friendStorage.getUserFriends(user1.getId()).size());
        assertTrue(friendStorage.getUserFriends(user1.getId()).contains(user2.getId()));
    }

    @Test
    public void getUserFriends() {
        user1 = userStorage.add(user1);
        assertEquals(0, user1.getFriends().size());
    }
}
