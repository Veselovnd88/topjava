package ru.javawebinar.topjava.util;

import org.assertj.core.api.Assertions;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MealTestData {

    public static final int USER_MEAL_ID = AbstractBaseEntity.START_SEQ + 3;

    public static final int ADMIN_MEAL_ID = AbstractBaseEntity.START_SEQ + 10;

    public static final int NOT_FOUND_MEAL_ID = AbstractBaseEntity.START_SEQ + 100;

    public static final Meal userMeal = new Meal(USER_MEAL_ID, LocalDateTime.of(2024, 2, 14, 10, 0, 6), "user meal breakFast", 500);

    public static final Meal adminMeal = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2024, 2, 14, 10, 0, 6), "admin meal breakFast", 500);

    public static Meal getNewUserMeal() {
        return new Meal(null, LocalDateTime.of(2024, 2, 7, 10, 0, 6), "user's meal", 100);
    }

    public static Meal getUpdatedUserMeal(int id, String updatedDesc) {
        Meal meal = getNewUserMeal();
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

    public static void checkMealsListWithSizeContainsSorting(List<Meal> actual, int size, Meal... expected) {
        Assertions.assertThat(actual).hasSize(size).contains(expected)
                .isSortedAccordingTo(Comparator.comparing(Meal::getDateTime).reversed());
    }
}
