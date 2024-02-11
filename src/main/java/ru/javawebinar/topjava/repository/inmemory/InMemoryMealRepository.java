package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> this.save(m, 1));
        this.save(new Meal(LocalDateTime.now().minusDays(5), "user3 Meal", 100, 3), 3);
        this.save(new Meal(LocalDateTime.now().minusDays(5), "user2 Meal", 100, 3), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMealMap = repository.computeIfAbsent(userId, id -> new ConcurrentHashMap<>());
        meal.setUserId(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMealMap.putIfAbsent(meal.getId(), meal);
            repository.putIfAbsent(userId, userMealMap);
            return meal;
        }
        // handle case: update, but not present in storage
        else {
            if (!userMealMap.containsKey(meal.getId())) {
                return null;
            } else {
                return userMealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
            }
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMealMap = repository.get(userId);
        if (userMealMap == null) {
            return false;
        } else {
            return userMealMap.remove(id) != null;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMealMap = repository.get(userId);
        if (userMealMap == null) {
            return null;
        }
        return userMealMap.get(id);
    }

    @Override
    public List<Meal> getAllFiltered(int userId, LocalDateTime startDate, LocalDateTime endDate) {
        return getAllWithPredicate(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDate, endDate));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllWithPredicate(userId, meal -> true);
    }

    private List<Meal> getAllWithPredicate(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> userMealMap = repository.get(userId);
        if (userMealMap == null) {
            return Collections.emptyList();
        }
        return userMealMap.values().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}

