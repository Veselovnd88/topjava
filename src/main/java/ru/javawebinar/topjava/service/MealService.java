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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class MealService {
    private static final Logger log = LoggerFactory.getLogger(MealService.class);

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        log.info("Saving meal");
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        log.info("Deleting meal with id: {}", id);
        ValidationUtil.checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, int userId) {
        log.info("Retrieving meal from repository");
        return ValidationUtil.checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<MealTo> getAll(int userId, int calories) {
        log.info("Retrieving all meals from repository");
        List<Meal> meals = repository.getAll(userId);
        return MealsUtil.getTos(meals, calories);
    }

    public List<MealTo> getAllFiltered(int userId, int calories, LocalDate startDate, LocalDate endDate,
                                       LocalTime startTime, LocalTime endTime) {
        log.info("Retrieving all meals from repository with filter options");
        List<Meal> meals = repository.getAllFiltered(userId, startDate, endDate);
        return MealsUtil.getFilteredTos(meals, calories, startTime, endTime);
    }

    public void update(Meal meal, int mealId, int userId) {
        log.info("Updating meal with id: {}", meal.getId());
        meal.setId(mealId);
        ValidationUtil.checkNotFoundWithId(repository.save(meal, userId), mealId);
    }
}
