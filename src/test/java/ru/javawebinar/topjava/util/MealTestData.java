package ru.javawebinar.topjava.util;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

public class MealTestData {

    public static final int USER_MEAL_ID = AbstractBaseEntity.START_SEQ + 3;

    public static final int ADMIN_MEAL_ID = AbstractBaseEntity.START_SEQ + 10;

    public static final int GUEST_MEAL_ID = AbstractBaseEntity.START_SEQ + 17;

    public static final int NOT_FOUND_MEAL_ID = AbstractBaseEntity.START_SEQ + 100;

    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2024, 2, 14, 10, 0, 6);

    public static final Meal userMeal = new Meal(USER_MEAL_ID, LOCAL_DATE_TIME, "user meal breakFast", 500);

    public static final Meal adminMeal = new Meal(ADMIN_MEAL_ID, LOCAL_DATE_TIME, "admin meal breakFast", 500);

    public static final Meal guestMeal = new Meal(GUEST_MEAL_ID, LOCAL_DATE_TIME, "guest meal breakFast", 500);

    public static Meal getNewUserMeal(int userId) {
        return new Meal(null, LOCAL_DATE_TIME.minusWeeks(1), "user's " + userId + " meal", 100);
    }

    public static Meal getUpdatedUserMeal(int id, int userId, String updatedDesc) {
        Meal meal = getNewUserMeal(userId);
        meal.setId(id);
        meal.setDescription(updatedDesc);
        return meal;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        Assertions.assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
