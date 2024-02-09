package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;
@Service
public class MealService {
    private static final Logger log = LoggerFactory.getLogger(MealService.class);

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public MealTo create(Meal meal, int userId) {
        log.info("Saving meal");
        Meal savedMeal = repository.save(meal, userId);
        return MealsUtil.createTo(savedMeal, savedMeal.getCalories() > MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public void delete(int id, int userId) {
        log.info("Deleting meal with id: {}", id);
        ValidationUtil.checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public MealTo get(int id, int userId) {
        log.info("Retrieving meal from repository");
        Meal meal = ValidationUtil.checkNotFoundWithId(repository.get(id, userId), id);
        return MealsUtil.createTo(meal, meal.getCalories() > MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAll(int userId) {
        log.info("Retrieving all meals from repository");
        List<Meal> meals = repository.getAll(userId);
        return MealsUtil.getTos(meals, MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public void update(Meal meal, int userId) {
        log.info("Updating meal with id: {}", meal.getId());
        ValidationUtil.checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }
}
