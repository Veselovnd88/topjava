package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealCollector implements Collector<UserMeal, Map<LocalDate, List<UserMeal>>,
        List<UserMealWithExcess>> {

    private final int caloriesPerDay;

    public UserMealCollector(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public static UserMealCollector toUserMealWithExcess(int caloriesPerDay) {
        return new UserMealCollector(caloriesPerDay);
    }

    @Override
    public Supplier<Map<LocalDate, List<UserMeal>>> supplier() {
        return HashMap::new; //container to hold stream elements
    }

    @Override
    public BiConsumer<Map<LocalDate, List<UserMeal>>, UserMeal> accumulator() {
        //logic of accumulator, how new values from stream should be accumulated in passed collection
        return ((localDateListMap, userMeal) -> localDateListMap.merge(userMeal.getDateTime().toLocalDate(),
                Collections.singletonList(userMeal),
                (ov, nv) -> {
                    List<UserMeal> old = new ArrayList<>(ov);
                    old.addAll(nv);
                    return new ArrayList<>(old);
                }));
    }

    @Override
    public BinaryOperator<Map<LocalDate, List<UserMeal>>> combiner() {
        // not implemented, need for parallel stream
        return null;
    }


    @Override
    public Function<Map<LocalDate, List<UserMeal>>, List<UserMealWithExcess>> finisher() {
        //apply additional logic and convert to final collection
        return (localDateListMap -> localDateListMap.values().stream().flatMap(lst -> {
            boolean isExceed = lst.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
            return lst.stream().map(userMeal -> UserMealsUtil.convertToUserMealWithExcess(userMeal, isExceed));
        }).collect(Collectors.toList()));
    }

    @Override
    public Set<Characteristics> characteristics() {
        Set<Characteristics> characteristics = new HashSet<>();
        characteristics.add(Characteristics.UNORDERED);
        return characteristics;
    }
}
