package ru.javawebinar.topjava.repository.inmemory;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;

public class InMemoryUserRepositoryTest {

    UserRepository userRepository = new InMemoryUserRepository();

    @Test
    public void getByEmail_IfOnlyOneMatch_ReturnOneUser() {
        userRepository.save(new User(null, "name", "email@email.com", "strongpass", Role.ADMIN));
        userRepository.save(new User(null, "name", "not@email.com", "strongpass", Role.ADMIN));

        User byEmail = userRepository.getByEmail("email@email.com");

        Assertions.assertThat(byEmail).isNotNull();
        Assertions.assertThat(byEmail).extracting(User::getEmail).isEqualTo("email@email.com");
    }

    @Test
    public void getByEmail_NoMatches_ReturnNull() {
        User byEmail = userRepository.getByEmail("email@email.com");

        Assertions.assertThat(byEmail).isNull();
    }

    @Test
    public void getByAll_IfTwoUsers_ReturnSortedList() {
        userRepository.save(new User(null, "a", "email@email.com", "strongpass", Role.ADMIN));
        userRepository.save(new User(null, "b", "not@email.com", "strongpass", Role.ADMIN));

        List<User> allUsers = userRepository.getAll();

        Assertions.assertThat(allUsers).hasSize(2);
        Assertions.assertThat(allUsers.get(0)).extracting(User::getName).isEqualTo("a");
        Assertions.assertThat(allUsers.get(1)).extracting(User::getName).isEqualTo("b");
    }
}
