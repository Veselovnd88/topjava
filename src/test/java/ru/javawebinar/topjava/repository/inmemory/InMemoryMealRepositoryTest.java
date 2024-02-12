package ru.javawebinar.topjava.repository.inmemory;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

public class InMemoryMealRepositoryTest {

    MealRepository mealRepository = new InMemoryMealRepository();

    @Test
    public void save_NoUserAndHisMealsInRepo() {
        int userId = 5;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000);

        Meal saved = mealRepository.save(meal, userId);

        Assertions.assertThat(saved).isNotNull().extracting(Meal::getDescription).isEqualTo("desc");
    }

    @Test
    public void save_NotBelongToUser_UpdateAndReturn() {
        int userId = 5;
        int anotherUserId = 10;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000);
        Meal meal2 = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000);
        Meal saved = mealRepository.save(meal, userId);
        mealRepository.save(meal2, userId);
        Meal mealForUpdate = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000);
        mealForUpdate.setId(saved.getId());

        Meal updatedMeal = mealRepository.save(mealForUpdate, anotherUserId);

        Assertions.assertThat(updatedMeal).isNull();
        Meal userMeal = mealRepository.get(saved.getId(), userId);
        Assertions.assertThat(userMeal).isNotNull();
    }

    @Test
    public void save_AllOk_UpdateAndReturn() {
        int userId = 5;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000);
        Meal meal2 = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000);
        Meal saved = mealRepository.save(meal, userId);
        mealRepository.save(meal2, userId);
        Meal mealForUpdate = new Meal(LocalDateTime.now().plusDays(1), "change", 1000);
        mealForUpdate.setId(saved.getId());

        Meal updated = mealRepository.save(mealForUpdate, userId);

        Assertions.assertThat(updated).isNotNull().extracting(Meal::getDescription).isEqualTo("change");
    }

    @Test
    public void delete_IfMealIdNull_ReturnFalse() {
        boolean delete = mealRepository.delete(0, 0);

        Assertions.assertThat(delete).isFalse();
    }

    @Test
    public void delete_IfMealDoesntBelongToUser_ReturnFalse() {
        int userId = 5;
        int anotherUserId = 10;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000);
        Meal saved = mealRepository.save(meal, userId);

        boolean delete = mealRepository.delete(saved.getId(), anotherUserId);

        Assertions.assertThat(delete).isFalse();
    }

    @Test
    public void delete_AllOk_ReturnTrue() {
        int userId = 1;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000);
        Meal saved = mealRepository.save(meal, userId);

        boolean delete = mealRepository.delete(saved.getId(), userId);

        Assertions.assertThat(delete).isTrue();
    }

    @Test
    public void getAll_AllOk_ReturnSortedList() {
        int userId = 1;
        Meal meal = new Meal(LocalDateTime.now().minusDays(1), "desc", 1000);
        Meal meal2 = new Meal(LocalDateTime.now(), "desc", 1000);
        Meal saved = mealRepository.save(meal, userId);
        Meal saved2 = mealRepository.save(meal2, userId);

        List<Meal> allMeals = mealRepository.getAll(userId);

        Assertions.assertThat(allMeals).isNotNull().contains(saved, saved2);
        Assertions.assertThat(allMeals.get(0)).extracting(Meal::getId).isEqualTo(saved2.getId());
        Assertions.assertThat(allMeals.get(1)).extracting(Meal::getId).isEqualTo(saved.getId());
    }

    @Test
    public void getAll_NotBelongToUser_ReturnEmptyList() {
        int userId = 5;
        int anotherUserId = 10;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000);
        Meal meal2 = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000);
        mealRepository.save(meal, userId);
        mealRepository.save(meal2, userId);

        List<Meal> allMeals = mealRepository.getAll(anotherUserId);

        Assertions.assertThat(allMeals).isEmpty();
    }

    @Test
    public void getAllFiltered_AllOk_ReturnSortedList() {
        int userId = 1;
        Meal meal = new Meal(LocalDateTime.now().minusDays(2), "desc", 1000);
        Meal meal2 = new Meal(LocalDateTime.now(), "desc", 1000);
        mealRepository.save(meal, userId);
        Meal saved2 = mealRepository.save(meal2, userId);

        List<Meal> allMeals = mealRepository.getAllFiltered(userId, LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2));

        Assertions.assertThat(allMeals).isNotNull().hasSize(1).contains(saved2);
        Assertions.assertThat(allMeals.get(0)).extracting(Meal::getId).isEqualTo(saved2.getId());
    }

    @Test
    public void getAllFiltered_MealWithMaxLocalDate_ReturnSortedList() {
        int userId = 1;
        Meal meal = new Meal(LocalDateTime.MAX.minusNanos(1), "desc", 1000);
        mealRepository.save(meal, userId);

        List<Meal> allMeals = mealRepository.getAllFiltered(userId, LocalDateTime.MAX.minusDays(1),
                LocalDateTime.MAX);
        Assertions.assertThat(allMeals).hasSize(1);
    }
}
