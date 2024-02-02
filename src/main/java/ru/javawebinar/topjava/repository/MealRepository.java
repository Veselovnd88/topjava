package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.Optional;

public interface MealRepository {

    Optional<Meal> findById(Integer mealId);

    List<Meal> findAll();

    Meal update(Meal mealToUpdate);

    Meal save(Meal meal);

    Meal deleteById(Integer mealId);

}
