package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;

public class MealService {
    private static final Logger log = LoggerFactory.getLogger(MealService.class);

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        log.info("Saving meal");
        return repository.save(meal);
    }

    public void delete(int id) {
        log.info("Deleting meal with id: {}", id);
        ValidationUtil.checkNotFoundWithId(repository.delete(id), id);
    }

    public Meal get(int id) {
        log.info("Retrieving meal from repository");
        return ValidationUtil.checkNotFoundWithId(repository.get(id), id);
    }

    public List<Meal> getAll() {
        log.info("Retrieving all meals from repository");
        return repository.getAll();
    }

    public void update(Meal meal) {
        log.info("Updating meal with id: {}", meal.getId());
        ValidationUtil.checkNotFoundWithId(repository.save(meal), meal.getId());
    }
}
