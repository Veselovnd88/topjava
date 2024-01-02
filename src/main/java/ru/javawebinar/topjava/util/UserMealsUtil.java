package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(13, 15), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println("---------");
        filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(13, 15), 2000)
                .forEach(System.out::println);
        System.out.println("------Cycle with one pass (Consumer)-----");
        filteredByCycleWithConsumer(meals, LocalTime.of(7, 0), LocalTime.of(13, 15), 2000)
                .forEach(System.out::println);
        System.out.println("------Cycle with one pass (Predicate)-----");
        filteredByCycleWithPredicate(meals, LocalTime.of(7, 0), LocalTime.of(13, 15), 2000)
                .forEach(System.out::println);
        System.out.println("------Stream with one pass");
        filteredByStreamsWithOnePass(meals, LocalTime.of(7, 0), LocalTime.of(13, 15), 2000)
                .forEach(System.out::println);
        System.out.println("------Stream with Collecting and then");
        filteredByStreamsWithOnePassCollectingAndThen(meals, LocalTime.of(7, 0), LocalTime.of(13, 15), 2000)
                .forEach(System.out::println);
        System.out.println("-----Stream with custom collector");
        filteredByStreamsWithOnePassWithCustomCollector(meals, LocalTime.of(7, 0), LocalTime.of(13, 15), 2000)
                .forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> caloriesPerDayMap = new HashMap<>();
        meals.forEach(m -> {
            LocalDate localDate = m.getDateTime().toLocalDate();
            int calories = m.getCalories();
            Integer todayCalories = caloriesPerDayMap.getOrDefault(localDate, 0);
            caloriesPerDayMap.put(localDate, todayCalories + calories);
        });
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                UserMealWithExcess mealWithExcess = convertToUserMealWithExcess(meal,
                        caloriesPerDayMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay);
                mealsWithExcess.add(mealWithExcess);
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(),
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream().filter(userMeal -> TimeUtil.isBetweenHalfOpen(
                        userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> convertToUserMealWithExcess(userMeal,
                        caloriesPerDayMap.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCycleWithConsumer(List<UserMeal> meals, LocalTime startTime,
                                                                       LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDateMap = new HashMap<>();
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        Consumer<Void> consumer = x -> {
        };
        for (UserMeal userMeal : meals) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            caloriesByDateMap.merge(localDate, userMeal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                consumer = consumer.andThen(b -> mealsWithExcess.add(
                        convertToUserMealWithExcess(userMeal, caloriesByDateMap.get(localDate) > caloriesPerDay)));
            }
        }
        consumer.accept(null);
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByCycleWithPredicate(List<UserMeal> meals, LocalTime startTime,
                                                                        LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesByDateMap = new HashMap<>();
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        Predicate<Boolean> predicate = b -> true;
        for (UserMeal userMeal : meals) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            caloriesByDateMap.merge(localDate, userMeal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                predicate = predicate.and(b -> mealsWithExcess.add(
                        convertToUserMealWithExcess(userMeal, caloriesByDateMap.get(localDate) > caloriesPerDay)));
            }
        }
        predicate.test(true);
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreamsWithOnePass(List<UserMeal> meals, LocalTime startTime,
                                                                        LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate())).values() //map
                .stream().flatMap(lst -> { // Stream<List<UserMeal>> go for each sublist and calculate total calories
                    boolean isExceed = lst.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                    return lst.stream().filter(userMeal -> TimeUtil.isBetweenHalfOpen(
                                    userMeal.getDateTime().toLocalTime(),
                                    startTime,
                                    endTime))
                            .map(userMeal -> convertToUserMealWithExcess(userMeal, isExceed));
                }).collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByStreamsWithOnePassCollectingAndThen(List<UserMeal> meals,
                                                                                         LocalTime startTime,
                                                                                         LocalTime endTime,
                                                                                         int caloriesPerDay) {
        Function<Map<LocalDate, List<UserMeal>>, List<UserMealWithExcess>> finisher = map ->
                map.values().stream().flatMap(
                        lst -> { // Stream<List<UserMeal>> go for each sublist and calculate total calories
                            boolean isExceed = lst.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                            return lst.stream().filter(userMeal -> TimeUtil.isBetweenHalfOpen(
                                            userMeal.getDateTime().toLocalTime(),
                                            startTime,
                                            endTime))
                                    .map(userMeal -> convertToUserMealWithExcess(userMeal, isExceed));
                        }).collect(Collectors.toList());
        return meals.stream().collect(Collectors.collectingAndThen(
                Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate()), finisher));
    }

    public static List<UserMealWithExcess> filteredByStreamsWithOnePassWithCustomCollector(List<UserMeal> meals,
                                                                                           LocalTime startTime,
                                                                                           LocalTime endTime,
                                                                                           int caloriesPerDay) {
        List<UserMealWithExcess> collect = meals.stream().collect(UserMealCollector.toUserMealWithExcess(caloriesPerDay));


        return collect.stream().filter(userMeal -> TimeUtil.isBetweenHalfOpen(
                userMeal.getDateTime().toLocalTime(),
                startTime,
                endTime)).collect(Collectors.toList());
    }


    public static UserMealWithExcess convertToUserMealWithExcess(UserMeal userMeal, boolean excess) {
        return new UserMealWithExcess(userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                excess);
    }

}
