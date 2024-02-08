package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealRepository implements MealRepository {

    private static final Logger log = getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    private final AtomicInteger idGenerator = new AtomicInteger();

    public InMemoryMealRepository() {
        MealsUtil.hardCodedMeals().forEach(this::save);
    }

    @Override
    public Optional<Meal> findById(int id) {
        log.debug("Retrieving meal with [id: {}]", id);
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Meal> findAll() {
        log.debug("Retrieving all meals from storage");
        return new ArrayList<>(storage.values());
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(generateNextId());
            storage.put(meal.getId(), meal);
            log.info("Meal with [id: {}] successfully saved in storage", meal.getId());
            return meal;
        } else {
            Meal saved = storage.computeIfPresent(meal.getId(), (k, v) -> meal);
            log.info("Meal with [id: {}] successfully updated", meal.getId());
            return saved;
        }
    }

    @Override
    public void deleteById(int id) {
        log.info("Meal with [id: {}] was deleted", id);
        storage.remove(id);
    }

    private Integer generateNextId() {
        return idGenerator.incrementAndGet();
    }
}
