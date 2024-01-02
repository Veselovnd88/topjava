package ru.javawebinar.topjava.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;
import ru.javawebinar.topjava.util.util.MealListUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Stream;

class UserMealsUtilTest {

    @ParameterizedTest
    @MethodSource("getSetForTesting")
    void filteredByStreamsWithOnePassWithCustomCollector_ReturnCorrectList(List<UserMeal> meals, LocalTime startTime,
                                                                           LocalTime endTime, int calories,
                                                                           LocalDate dateWithExcess,
                                                                           LocalDate dateWithoutExcess,
                                                                           int resultListSize) {
        List<UserMealWithExcess> resultList = UserMealsUtil.filteredByStreamsWithOnePassWithCustomCollector(
                meals, startTime, endTime, calories);

        checkResults(resultList, dateWithExcess, dateWithoutExcess, resultListSize);
    }

    @ParameterizedTest
    @MethodSource("getSetForTesting")
    void filteredByCycles_ReturnCorrectList(List<UserMeal> meals, LocalTime startTime,
                                            LocalTime endTime, int calories,
                                            LocalDate dateWithExcess,
                                            LocalDate dateWithoutExcess,
                                            int resultListSize) {
        List<UserMealWithExcess> resultList = UserMealsUtil.filteredByCycles(meals, startTime, endTime, calories);

        checkResults(resultList, dateWithExcess, dateWithoutExcess, resultListSize);
    }

    @ParameterizedTest
    @MethodSource("getSetForTesting")
    void filteredByStreams_ReturnCorrectList(List<UserMeal> meals, LocalTime startTime,
                                             LocalTime endTime, int calories,
                                             LocalDate dateWithExcess,
                                             LocalDate dateWithoutExcess,
                                             int resultListSize) {
        List<UserMealWithExcess> resultList = UserMealsUtil.filteredByStreams(meals, startTime, endTime, calories);

        checkResults(resultList, dateWithExcess, dateWithoutExcess, resultListSize);
    }

    @ParameterizedTest
    @MethodSource("getSetForTesting")
    void filteredByCycleWithConsumer_ReturnCorrectList(List<UserMeal> meals, LocalTime startTime,
                                                       LocalTime endTime, int calories,
                                                       LocalDate dateWithExcess,
                                                       LocalDate dateWithoutExcess,
                                                       int resultListSize) {
        List<UserMealWithExcess> resultList = UserMealsUtil.filteredByCycleWithConsumer(
                meals, startTime, endTime, calories);

        checkResults(resultList, dateWithExcess, dateWithoutExcess, resultListSize);
    }

    @ParameterizedTest
    @MethodSource("getSetForTesting")
    void filteredByCycleWithPredicate_ReturnCorrectList(List<UserMeal> meals, LocalTime startTime,
                                                        LocalTime endTime, int calories,
                                                        LocalDate dateWithExcess,
                                                        LocalDate dateWithoutExcess,
                                                        int resultListSize) {
        List<UserMealWithExcess> resultList = UserMealsUtil.filteredByCycleWithPredicate(
                meals, startTime, endTime, calories);

        checkResults(resultList, dateWithExcess, dateWithoutExcess, resultListSize);
    }

    @ParameterizedTest
    @MethodSource("getSetForTesting")
    void filteredByStreamsWithOnePass_ReturnCorrectList(List<UserMeal> meals, LocalTime startTime,
                                                        LocalTime endTime, int calories,
                                                        LocalDate dateWithExcess,
                                                        LocalDate dateWithoutExcess,
                                                        int resultListSize) {
        List<UserMealWithExcess> resultList = UserMealsUtil.filteredByStreamsWithOnePass(
                meals, startTime, endTime, calories);

        checkResults(resultList, dateWithExcess, dateWithoutExcess, resultListSize);
    }

    @ParameterizedTest
    @MethodSource("getSetForTesting")
    void filteredByStreamsWithOnePassCollectingAndThen_ReturnCorrectList(List<UserMeal> meals, LocalTime startTime,
                                                                         LocalTime endTime,
                                                                         int calories, LocalDate dateWithExcess,
                                                                         LocalDate dateWithoutExcess,
                                                                         int resultListSize) {
        List<UserMealWithExcess> resultList = UserMealsUtil.filteredByStreamsWithOnePassCollectingAndThen(
                meals, startTime, endTime, calories);

        checkResults(resultList, dateWithExcess, dateWithoutExcess, resultListSize);
    }

    private void checkResults(List<UserMealWithExcess> resultList, LocalDate dateWithExcess,
                              LocalDate dateWithoutExcess, int qnt) {
        for (UserMealWithExcess meal : resultList) {
            if (meal.getDateTime().toLocalDate().isEqual(dateWithExcess)) {
                Assertions.assertTrue(meal.isExcess());
            }
            if (meal.getDateTime().toLocalDate().isEqual(dateWithoutExcess)) {
                Assertions.assertFalse(meal.isExcess());
            }
        }
        Assertions.assertEquals(qnt, resultList.size());
    }


    private static Stream<Arguments> getSetForTesting() {
        return Stream.of(
                Arguments.of(
                        MealListUtils.baseMeals,
                        LocalTime.of(7, 0), //start time
                        LocalTime.of(13, 15), //end time
                        2000, //calories
                        LocalDate.of(2020, Month.JANUARY, 31), //date with excess
                        LocalDate.of(2020, Month.JANUARY, 30), //date without excess
                        4 //list size
                ),
                Arguments.of(MealListUtils.veryBigList(),
                        LocalTime.of(7, 0), //start time
                        LocalTime.of(13, 15), //end time
                        2000, //calories
                        LocalDate.of(2020, Month.JANUARY, 31), //date with excess
                        LocalDate.of(2020, Month.JANUARY, 30), //date without excess
                        10004 //list size
                )
        );
    }


}