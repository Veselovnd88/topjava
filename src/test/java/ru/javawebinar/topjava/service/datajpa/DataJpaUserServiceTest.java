package ru.javawebinar.topjava.service.datajpa;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.Collections;

import static ru.javawebinar.topjava.UserTestData.GUEST_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @BeforeClass
    public static void setUp() {
        testLog.info("Tests for UserService with Data Jpa repository launched");
    }

    @Test
    public void getWithMeals() {
        User user = super.service.getWithMeals(USER_ID);

        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.user);
        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.meals);
    }

    @Test
    public void getUserWithoutMeals() {
        User user = super.service.getWithMeals(GUEST_ID);

        UserTestData.USER_MATCHER.assertMatch(user, UserTestData.guest);
        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), Collections.emptyList());
    }
}
