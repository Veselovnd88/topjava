package ru.javawebinar.topjava.util;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

public class MealTestData {

    public static final int USER_MEAL_ID = AbstractBaseEntity.START_SEQ + 3;

    public static final int ADMIN_MEAL_ID = AbstractBaseEntity.START_SEQ + 4;

    public static final int GUEST_MEAL_ID = AbstractBaseEntity.START_SEQ + 5;

    public static final int NOT_FOUND_MEAL_ID = AbstractBaseEntity.START_SEQ + 100;

    public static final LocalDateTime LDT_USER = LocalDateTime.of(2024, 2, 14, 20, 0, 6);

    public static final LocalDateTime LDT_ADMIN = LocalDateTime.of(2024, 2, 15, 20, 0, 6);

    public static final LocalDateTime LDT_GUEST = LocalDateTime.of(2024, 2, 13, 20, 0, 6);

    public static final Meal USER_MEAL = new Meal(USER_MEAL_ID, LDT_USER, "user meal", 1000);

    public static final Meal ADMIN_MEAL = new Meal(ADMIN_MEAL_ID, LDT_ADMIN, "admin meal", 500);

    public static final Meal GUEST_MEAL = new Meal(GUEST_MEAL_ID, LDT_GUEST, "guest meal", 600);

    public static Meal getNewUserMeal(int userId) {
        return new Meal(null, LDT_USER.plusDays(1), "user's " + userId + " meal", 100);
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
