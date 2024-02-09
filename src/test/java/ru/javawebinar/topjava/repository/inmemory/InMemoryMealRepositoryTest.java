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
    public void delete_IfMealIdNull_ReturnFalse() {
        boolean delete = mealRepository.delete(0, 0);

        Assertions.assertThat(delete).isFalse();
    }

    @Test
    public void delete_IfMealDoesntBelongToUser_ReturnFalse() {
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000, 2);
        Meal saved = mealRepository.save(meal, 2);

        boolean delete = mealRepository.delete(saved.getId(), 1);

        Assertions.assertThat(delete).isFalse();
    }

    @Test
    public void delete_IfMealDoesntHaveUserIdTo_ReturnFalse() {
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000, null);
        Meal saved = mealRepository.save(meal, 1);

        boolean delete = mealRepository.delete(saved.getId(), 1);

        Assertions.assertThat(delete).isFalse();
    }

    @Test
    public void delete_AllOk_ReturnTrue() {
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000, 1);
        Meal saved = mealRepository.save(meal, 1);

        boolean delete = mealRepository.delete(saved.getId(), 1);

        Assertions.assertThat(delete).isTrue();
    }

    @Test
    public void getAll_AllOk_ReturnSortedLust() {
        int userId = 1;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000, userId);
        Meal meal2 = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000, userId);
        Meal saved = mealRepository.save(meal, userId);
        Meal saved2 = mealRepository.save(meal2, userId);

        List<Meal> allMeals = mealRepository.getAll(userId);

        Assertions.assertThat(allMeals).isNotNull().contains(saved, saved2);
        Assertions.assertThat(allMeals.get(0)).extracting(Meal::getId).isEqualTo(saved2.getId());
        Assertions.assertThat(allMeals.get(1)).extracting(Meal::getId).isEqualTo(saved.getId());
    }

    @Test
    public void getAll_NotBelongToUser_ReturnSortedLust() {
        int userId = 2;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000, userId);
        Meal meal2 = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000, userId);
        mealRepository.save(meal, userId);
        mealRepository.save(meal2, userId);

        List<Meal> allMeals = mealRepository.getAll(1);

        Assertions.assertThat(allMeals).isEmpty();
    }

    @Test
    public void save_NotBelongToUser_UpdateAndReturn() {
        int userId = 2;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000, userId);
        Meal meal2 = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000, userId);
        Meal saved = mealRepository.save(meal, userId);
        mealRepository.save(meal2, userId);
        Meal mealForUpdate = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000, 3);
        mealForUpdate.setId(saved.getUserId());

        Meal updated = mealRepository.save(mealForUpdate, 1);

        Assertions.assertThat(updated).isNull();
    }

    @Test
    public void save_NotBelongToUser_ReturnNull() {
        int userId = 2;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000, userId);
        Meal meal2 = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000, userId);
        Meal saved = mealRepository.save(meal, userId);
        mealRepository.save(meal2, userId);
        Meal mealForUpdate = new Meal(LocalDateTime.now().plusDays(1), "change", 1000, 3);
        mealForUpdate.setId(saved.getUserId());

        Meal updated = mealRepository.save(mealForUpdate, 1);

        Assertions.assertThat(updated).isNull();
        Assertions.assertThat(saved.getDescription()).isEqualTo("desc");
    }

    @Test
    public void save_AllOk_UpdateAndReturn() {
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000, 1);
        Meal meal2 = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000, 2);
        Meal saved = mealRepository.save(meal, 1);
        mealRepository.save(meal2, 1);
        Meal mealForUpdate = new Meal(LocalDateTime.now().plusDays(1), "change", 1000, 1);
        mealForUpdate.setId(saved.getId());

        Meal updated = mealRepository.save(mealForUpdate, 1);

        Assertions.assertThat(updated).isNotNull().extracting(Meal::getDescription).isEqualTo("change");
    }

    @Test
    public void save_MealDoesntBelongToUser_ReturnNull() {
        int userId = 2;
        Meal meal = new Meal(LocalDateTime.now(), "desc", 1000, userId);
        Meal meal2 = new Meal(LocalDateTime.now().plusDays(1), "desc", 1000, userId);
        Meal saved = mealRepository.save(meal, 1);
        mealRepository.save(meal2, 1);
        Meal mealForUpdate = new Meal(LocalDateTime.now().plusDays(1), "change", 1000, userId);
        mealForUpdate.setId(saved.getId());

        Meal updated = mealRepository.save(mealForUpdate, 1);

        Assertions.assertThat(updated).isNull();
    }
}
