package ru.javawebinar.topjava.service.user;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;

import java.util.Collections;

import static ru.javawebinar.topjava.UserTestData.GUEST_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceBaseTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceDataJpaTest.class);

    @BeforeClass
    public static void setUp() {
        log.info("Tests for UserService with Data Jpa repository launched");
    }

    @Test
    public void getWithMeals() {
        User user = super.service.get(USER_ID);

        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.meals);
    }

    @Test
    public void getUserWithoutMeals() {
        User user = super.service.get(GUEST_ID);

        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), Collections.emptyList());
    }
}
