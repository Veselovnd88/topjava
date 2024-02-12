package ru.javawebinar.topjava.web.meal;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MealRestControllerTest {
    MealRepository mealRepository = new InMemoryMealRepository();
    MealRestController mealRestController = new MealRestController(new MealService(mealRepository));

    @Test
    public void getAllFilteredWithMaxLocalDate() {
        int userId = 1;
        mealRepository.save(new Meal(LocalDateTime.MAX.minusSeconds(1), "desc", 1000), userId);
        List<MealTo> allFiltered = mealRestController.getAllFiltered(LocalDate.MAX.minusDays(2),
                LocalDate.MAX, LocalTime.MIN, LocalTime.MAX);
        Assertions.assertThat(allFiltered).hasSize(1);
    }

    @Test
    public void getAllFilteredWithNotMaxLocalDate() {
        int userId = 1;
        mealRepository.save(new Meal(LocalDateTime.MAX.minusDays(1).minusSeconds(1), "desc", 1000), userId);
        List<MealTo> allFiltered = mealRestController.getAllFiltered(LocalDate.MAX.minusDays(2),
                LocalDate.MAX.minusDays(1), LocalTime.MIN, LocalTime.MAX);
        Assertions.assertThat(allFiltered).hasSize(1);
    }
}