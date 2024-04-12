package ru.javawebinar.topjava.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Collections;
import java.util.Date;
import java.util.stream.Stream;

public class InvalidUserArgumentsProvider implements ArgumentsProvider {

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String CALORIES = "caloriesPerDay";


    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        User blankName = new User(1, "", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
        User noName = new User(1, "", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
        User shortName = new User(1, "N", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
        User longName = new User(1, "1".repeat(129), "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
        User blankEmail = new User(1, "New", "", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
        User wrongEmail = new User(1, "New", "12312341234", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
        User longEmail = new User(1, "New", "new@gmail.com".repeat(40), "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
        User blankPassword = new User(1, "New", "new@gmail.com", "", 1555, false, new Date(), Collections.singleton(Role.USER));
        User shortPassword = new User(1, "New", "new@gmail.com", "1234", 1555, false, new Date(), Collections.singleton(Role.USER));
        User longPassword = new User(1, "New", "new@gmail.com", "1".repeat(129), 1555, false, new Date(), Collections.singleton(Role.USER));
        User lessCalories = new User(1, "New", "new@gmail.com", "newPass", 5, false, new Date(), Collections.singleton(Role.USER));
        User tooMuchCalories = new User(1, "New", "new@gmail.com", "newPass", 100001, false, new Date(), Collections.singleton(Role.USER));

        return Stream.of(
                Arguments.of(blankName, NAME),
                Arguments.of(noName, NAME),
                Arguments.of(shortName, NAME),
                Arguments.of(longName, NAME),
                Arguments.of(blankEmail, EMAIL),
                Arguments.of(wrongEmail, EMAIL),
                Arguments.of(longEmail, EMAIL),
                Arguments.of(blankPassword, PASSWORD),
                Arguments.of(shortPassword, PASSWORD),
                Arguments.of(longPassword, PASSWORD),
                Arguments.of(lessCalories, CALORIES),
                Arguments.of(tooMuchCalories, CALORIES)
        );
    }
}
