package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.Optional;

public interface MealRepository {

    Optional<Meal> findById(Integer id);

    List<Meal> findAll();

    Meal update(Meal mealToUpdate);

    Meal save(Meal meal);

    void deleteById(Integer id);

}
