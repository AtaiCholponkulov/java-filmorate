package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    
    private final UserDbStorage userStorage;
    private User user1 = new User();

    @BeforeEach
    public void beforeEach() {
        user1.setLogin("dolore");
        user1.setName("Nick Name");
        user1.setEmail("mail@mail.ru");
        user1.setBirthday(LocalDate.of(1946, 8, 20));
    }

    @Test
    public void userDaoShouldAddUser() {
        assertEquals(0, user1.getId());
        user1 = userStorage.add(user1);
        assertEquals(userStorage.getAll().size(), user1.getId());
    }

    @Test
    public void userDaoShouldReturnUser() {
        user1 = userStorage.add(user1);
        User dbUser = userStorage.get(user1.getId());

        assertEquals(user1.getId(), dbUser.getId());
        assertEquals(user1.getLogin(), dbUser.getLogin());
        assertEquals(user1.getName(), dbUser.getName());
        assertEquals(user1.getEmail(), dbUser.getEmail());
        assertEquals(user1.getBirthday(), dbUser.getBirthday());
    }

    @Test
    public void userDaoShouldUpdateUser() {
        user1 = userStorage.add(user1);

        User user1Updated = new User();
        user1Updated.setId(user1.getId());
        user1Updated.setLogin("doloreUpdate");
        user1Updated.setName("est adipisicing");
        user1Updated.setEmail("mail@yandex.ru");
        user1Updated.setBirthday(LocalDate.of(1976, 9, 20));

        userStorage.update(user1Updated);
        User dbUser1Updated = userStorage.get(user1.getId());

        assertEquals(user1Updated.getId(), dbUser1Updated.getId());
        assertEquals(user1Updated.getLogin(), dbUser1Updated.getLogin());
        assertEquals(user1Updated.getName(), dbUser1Updated.getName());
        assertEquals(user1Updated.getEmail(), dbUser1Updated.getEmail());
        assertEquals(user1Updated.getBirthday(), dbUser1Updated.getBirthday());
    }

    @Test
    public void getAll() {
        int initialSize = userStorage.getAll().size();
        userStorage.add(user1);
        assertEquals(initialSize + 1, userStorage.getAll().size());
    }
}