package ru.javawebinar.topjava.repository.impl;

import org.slf4j.Logger;
import ru.javawebinar.topjava.exception.ExceptionUtils;
import ru.javawebinar.topjava.exception.MealNotFoundException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class MealRepositoryImpl implements MealRepository {

    private static final Logger log = getLogger(MealRepositoryImpl.class);

    private final Map<Integer, Meal> storage = new ConcurrentHashMap<>();

    private final AtomicInteger idGenerator = new AtomicInteger();

    public MealRepositoryImpl() {
        MealsUtil.hardCodedMeals().forEach(this::save);
    }

    @Override
    public Optional<Meal> findById(Integer id) {
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
        Integer id = meal.getId();
        if (id == null) {
            meal.setId(generateNextId());
            storage.put(meal.getId(), meal);
            log.info("Meal with [id: {}] successfully saved in storage", meal.getId());
            return meal;
        } else {
            if (!storage.containsKey(id)) {
                String exceptionMessage = String.format(ExceptionUtils.MEAL_NOT_FOUND, id);
                log.warn(exceptionMessage);
                throw new MealNotFoundException(exceptionMessage);
            }
            Meal saved = storage.put(id, meal);
            log.info("Meal with [id: {}] successfully updated", id);
            return saved;
        }
    }

    @Override
    public void deleteById(Integer id) {
        log.info("Meal with [id: {}] was deleted", id);
        storage.remove(id);
    }

    private Integer generateNextId() {
        return idGenerator.addAndGet(1);
    }

}
