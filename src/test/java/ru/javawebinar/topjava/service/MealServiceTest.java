package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealTestData;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml",
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    MealService mealService;

    @Test
    public void create_AllOk_ReturnSavedMealWithId() {
        int userId = UserTestData.USER_ID;
        Meal newMeal = MealTestData.getNewUserMeal(userId);

        Meal savedMeal = mealService.create(newMeal, userId);

        Integer mealId = savedMeal.getId();
        newMeal.setId(mealId);
        MealTestData.assertMatch(savedMeal, newMeal);
        MealTestData.assertMatch(mealService.get(mealId, userId), savedMeal);
    }

    @Test
    public void create_SameDateTimeSameUser_ReturnNull() {
        int userId = UserTestData.USER_ID;
        Meal newMeal = MealTestData.getNewUserMeal(userId);
        newMeal.setDateTime(MealTestData.LDT_USER);
        Assertions.assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> mealService.create(newMeal, userId));
    }

    @Test
    public void get_ForExistingUserMeal_ReturnCorrectMeal() {
        Meal foundMeal = mealService.get(MealTestData.USER_MEAL_ID, UserTestData.USER_ID);

        MealTestData.assertMatch(foundMeal, MealTestData.USER_MEAL);
    }

    @Test
    public void get_ForExistingAdminMeal_ReturnCorrectMeal() {
        Meal foundMeal = mealService.get(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);

        MealTestData.assertMatch(foundMeal, MealTestData.ADMIN_MEAL);
    }

    @Test
    public void get_ForExistingGuestMeal_ReturnCorrectMeal() {
        Meal foundMeal = mealService.get(MealTestData.GUEST_MEAL_ID, UserTestData.GUEST_ID);

        MealTestData.assertMatch(foundMeal, MealTestData.GUEST_MEAL);
    }

    @Test
    public void get_ForNewSavedMeal_ReturnCorrectMeal() {
        int userId = UserTestData.USER_ID;
        Meal newMeal = MealTestData.getNewUserMeal(userId);
        Meal savedMeal = mealService.create(newMeal, userId);
        Integer mealId = savedMeal.getId();

        Meal foundMeal = mealService.get(mealId, userId);

        newMeal.setId(mealId);
        MealTestData.assertMatch(savedMeal, newMeal);
        MealTestData.assertMatch(foundMeal, savedMeal);
    }

    @Test
    public void get_WrongUser_ThrowNotFound() {
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.get(MealTestData.GUEST_MEAL_ID, UserTestData.ADMIN_ID))
                .withMessage("Not found entity with id=" + MealTestData.GUEST_MEAL_ID);
    }

    @Test
    public void get_NoSuchMeal_ThrowNotFound() {
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.get(MealTestData.NOT_FOUND_MEAL_ID, UserTestData.ADMIN_ID))
                .withMessage("Not found entity with id=" + MealTestData.NOT_FOUND_MEAL_ID);
    }

    @Test
    public void delete_AllOkForUser_Delete() {
        mealService.delete(MealTestData.USER_MEAL_ID, UserTestData.USER_ID);

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.get(MealTestData.USER_MEAL_ID, UserTestData.USER_ID))
                .withMessage("Not found entity with id=" + MealTestData.USER_MEAL_ID);
    }

    @Test
    public void delete_AllOkForAdmin_Delete() {
        mealService.delete(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.get(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID))
                .withMessage("Not found entity with id=" + MealTestData.ADMIN_MEAL_ID);
    }

    @Test
    public void delete_AllOkForGuest_Delete() {
        mealService.delete(MealTestData.GUEST_MEAL_ID, UserTestData.GUEST_ID);

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.get(MealTestData.GUEST_MEAL_ID, UserTestData.GUEST_ID))
                .withMessage("Not found entity with id=" + MealTestData.GUEST_MEAL_ID);
    }

    @Test
    public void delete_WrongUser_ThrowNotFound() {
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.delete(MealTestData.GUEST_MEAL_ID, UserTestData.ADMIN_ID))
                .withMessage("Not found entity with id=" + MealTestData.GUEST_MEAL_ID);
    }

    @Test
    public void delete_NoSuchMeal_ThrowNotFound() {
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.delete(MealTestData.NOT_FOUND_MEAL_ID, UserTestData.ADMIN_ID))
                .withMessage("Not found entity with id=" + MealTestData.NOT_FOUND_MEAL_ID);
    }

    @Test
    public void update_AllOkForUser_ReturnUpdatedMeal() {
        Meal mealToUpdate = MealTestData.getUpdatedUserMeal(MealTestData.USER_MEAL_ID, UserTestData.USER_ID, "updated");

        mealService.update(mealToUpdate, UserTestData.USER_ID);

        Meal updatedMeal = mealService.get(MealTestData.USER_MEAL_ID, UserTestData.USER_ID);
        MealTestData.assertMatch(updatedMeal, mealToUpdate);
    }

    @Test
    public void update_AllOkForAdmin_ReturnUpdatedMeal() {
        Meal mealToUpdate = MealTestData.getUpdatedUserMeal(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID, "updated");

        mealService.update(mealToUpdate, UserTestData.ADMIN_ID);

        Meal updatedMeal = mealService.get(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);
        MealTestData.assertMatch(updatedMeal, mealToUpdate);
    }

    @Test
    public void update_AllOkForGuest_ReturnUpdatedMeal() {
        Meal mealToUpdate = MealTestData.getUpdatedUserMeal(MealTestData.GUEST_MEAL_ID, UserTestData.GUEST_ID, "updated");

        mealService.update(mealToUpdate, UserTestData.GUEST_ID);

        Meal updatedMeal = mealService.get(MealTestData.GUEST_MEAL_ID, UserTestData.GUEST_ID);
        MealTestData.assertMatch(updatedMeal, mealToUpdate);
    }

    @Test
    public void update_WrongUser_ThrowNotFound() {
        Meal mealToUpdate = MealTestData.getUpdatedUserMeal(MealTestData.GUEST_MEAL_ID, UserTestData.GUEST_ID, "updated");

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.update(mealToUpdate, UserTestData.ADMIN_ID))
                .withMessage("Not found entity with id=" + MealTestData.GUEST_MEAL_ID);
    }

    @Test
    public void update_NoSuchMeal_ThrowNotFound() {
        Meal mealToUpdate = MealTestData.getUpdatedUserMeal(MealTestData.NOT_FOUND_MEAL_ID, UserTestData.USER_ID, "updated");

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.update(mealToUpdate, UserTestData.USER_ID))
                .withMessage("Not found entity with id=" + MealTestData.NOT_FOUND_MEAL_ID);
    }

    @Test
    public void getAll_AllOk_ReturnListWithMeals() {
        int userId = UserTestData.USER_ID;
        Meal newMeal = MealTestData.getNewUserMeal(userId);
        Meal savedMeal = mealService.create(newMeal, userId);

        List<Meal> meals = mealService.getAll(userId);

        MealTestData.assertMatch(meals, savedMeal, MealTestData.USER_MEAL);
    }

    @Test
    public void getAll_WrongUser_ReturnEmptyList() {
        List<Meal> meals = mealService.getAll(UserTestData.NOT_FOUND);

        Assertions.assertThat(meals).isEmpty();
    }

    @Test
    public void getBetweenInclusive_AllOkForUser_ReturnListOfMeals() {
        int userId = UserTestData.USER_ID;
        Meal newMeal1 = MealTestData.getNewUserMeal(userId);
        Meal savedMeal1 = mealService.create(newMeal1, userId);

        List<Meal> betweenInclusive = mealService.getBetweenInclusive(MealTestData.LDT_USER.toLocalDate(),
                MealTestData.LDT_USER.toLocalDate().plusDays(1), userId);

        MealTestData.assertMatch(betweenInclusive, savedMeal1, MealTestData.USER_MEAL);
    }

    @Test
    public void getBetweenInclusive_OneDateInBorders_ReturnListOfUserMeals() {
        int userId = UserTestData.USER_ID;
        Meal newMeal1 = MealTestData.getNewUserMeal(userId);
        mealService.create(newMeal1, userId);

        List<Meal> betweenInclusive = mealService.getBetweenInclusive(MealTestData.LDT_USER.toLocalDate(),
                MealTestData.LDT_USER.toLocalDate(), userId);

        MealTestData.assertMatch(betweenInclusive, MealTestData.USER_MEAL);
    }

    @Test
    public void getBetweenInclusive_NoMatches_ReturnEmptyList() {
        int userId = UserTestData.USER_ID;
        Meal newMeal1 = MealTestData.getNewUserMeal(userId);
        mealService.create(newMeal1, userId);

        List<Meal> betweenInclusive = mealService.getBetweenInclusive(MealTestData.LDT_USER.toLocalDate().minusDays(100),
                MealTestData.LDT_USER.toLocalDate().minusDays(50), userId);

        Assertions.assertThat(betweenInclusive).isEmpty();
    }

    @Test
    public void getBetweenInclusive_WrongUser_ReturnOnlyAdminsMeals() {
        int userId = UserTestData.USER_ID;
        Meal newMeal1 = MealTestData.getNewUserMeal(userId);
        mealService.create(newMeal1, userId);
        Meal newMeal2 = MealTestData.getNewUserMeal(userId);
        newMeal2.setDateTime(MealTestData.LDT_USER.plusDays(2));
        mealService.create(newMeal2, userId);

        List<Meal> betweenInclusive = mealService.getBetweenInclusive(MealTestData.LDT_USER.toLocalDate(),
                MealTestData.LDT_USER.toLocalDate().plusDays(2), UserTestData.ADMIN_ID);

        MealTestData.assertMatch(betweenInclusive, MealTestData.ADMIN_MEAL);
    }

    @Test
    public void getBetweenInclusive_MealsWithStartAndEndOfDay_ReturnListOfMeals() {
        int userId = UserTestData.USER_ID;
        Meal newMeal1 = MealTestData.getNewUserMeal(userId);
        newMeal1.setDateTime(MealTestData.LDT_USER.toLocalDate().atStartOfDay());
        Meal savedMeal1 = mealService.create(newMeal1, userId);
        Meal newMeal2 = MealTestData.getNewUserMeal(userId);
        newMeal2.setDateTime(MealTestData.LDT_USER.plusDays(3).toLocalDate().atStartOfDay().minusSeconds(1));
        Meal savedMeal2 = mealService.create(newMeal2, userId);

        List<Meal> betweenInclusive = mealService.getBetweenInclusive(MealTestData.LDT_USER.toLocalDate(),
                MealTestData.LDT_USER.toLocalDate().plusDays(2), userId);

        MealTestData.assertMatch(betweenInclusive, savedMeal2, MealTestData.USER_MEAL, savedMeal1);
    }
}
