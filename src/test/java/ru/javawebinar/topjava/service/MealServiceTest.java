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

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private MealService mealService;

    @Test
    public void create_AllOk_ReturnSavedMealWithId() {
        int userId = UserTestData.USER_ID;

        Meal savedMeal = mealService.create(MealTestData.getNewUserMeal(), userId);

        Meal newMealForCheck = MealTestData.getNewUserMeal();
        Integer mealId = savedMeal.getId();
        newMealForCheck.setId(mealId);
        MealTestData.assertMatch(savedMeal, newMealForCheck);
        MealTestData.assertMatch(mealService.get(mealId, userId), savedMeal);
    }

    @Test
    public void create_SameDateTimeSameUser_ThrowException() {
        int userId = UserTestData.USER_ID;
        Meal newMeal = MealTestData.getNewUserMeal();
        newMeal.setDateTime(LocalDateTime.of(2024, 2, 14, 10, 0, 6));
        Assertions.assertThatExceptionOfType(DataAccessException.class)
                .isThrownBy(() -> mealService.create(newMeal, userId));
    }

    @Test
    public void get_ForExistingUserMeal_ReturnCorrectMeal() {
        checkGetMealByIdAndUserId(MealTestData.USER_MEAL_ID, UserTestData.USER_ID, MealTestData.userMeal);
    }

    @Test
    public void get_ForExistingAdminMeal_ReturnCorrectMeal() {
        checkGetMealByIdAndUserId(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID, MealTestData.adminMeal);
    }

    private void checkGetMealByIdAndUserId(int mealId, int userId, Meal meal) {
        Meal foundMeal = mealService.get(mealId, userId);

        MealTestData.assertMatch(foundMeal, meal);
    }

    @Test
    public void get_WrongUser_ThrowNotFound() {
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.get(MealTestData.USER_MEAL_ID, UserTestData.ADMIN_ID))
                .withMessage(MealTestData.NOT_FOUND_MSG + MealTestData.USER_MEAL_ID);
    }

    @Test
    public void get_NoSuchMeal_ThrowNotFound() {
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.get(MealTestData.NOT_FOUND_MEAL_ID, UserTestData.ADMIN_ID))
                .withMessage(MealTestData.NOT_FOUND_MSG + MealTestData.NOT_FOUND_MEAL_ID);
    }

    @Test
    public void delete_AllOkForUser_Delete() {
        checkDeleteMealByIdAndUserId(MealTestData.USER_MEAL_ID, UserTestData.USER_ID);
    }

    @Test
    public void delete_AllOkForAdmin_Delete() {
        checkDeleteMealByIdAndUserId(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);
    }

    private void checkDeleteMealByIdAndUserId(int mealId, int userId) {
        mealService.delete(mealId, userId);

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.get(mealId, userId))
                .withMessage(MealTestData.NOT_FOUND_MSG + mealId);
    }

    @Test
    public void delete_WrongUser_ThrowNotFound() {
        checkDeleteMealByIdAndUserIdForException(MealTestData.USER_MEAL_ID, UserTestData.ADMIN_ID);
    }

    @Test
    public void delete_NoSuchMeal_ThrowNotFound() {
        checkDeleteMealByIdAndUserIdForException(MealTestData.NOT_FOUND_MEAL_ID, UserTestData.USER_ID);
    }

    private void checkDeleteMealByIdAndUserIdForException(int mealId, int userId) {
        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.delete(mealId, userId))
                .withMessage(MealTestData.NOT_FOUND_MSG + mealId);
    }

    @Test
    public void update_AllOkForUser_UpdateUserMeal() {
        checkUpdateMealByIdAndUserId(MealTestData.USER_MEAL_ID, UserTestData.USER_ID);
    }

    @Test
    public void update_AllOkForAdmin_UpdateAdminMeal() {
        checkUpdateMealByIdAndUserId(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);
    }

    private void checkUpdateMealByIdAndUserId(int mealId, int userId) {
        Meal mealToUpdate = MealTestData.getUpdatedUserMeal(mealId, "updated");

        mealService.update(mealToUpdate, userId);

        Meal updatedMeal = mealService.get(mealId, userId);
        MealTestData.assertMatch(updatedMeal, mealToUpdate);
    }

    @Test
    public void update_WrongUser_ThrowNotFound() {
        checkUpdateMealByIdAndUserIdForException(MealTestData.ADMIN_MEAL_ID, UserTestData.USER_ID);
    }

    @Test
    public void update_UserDoesntExists_ThrowNotFound() {
        checkUpdateMealByIdAndUserIdForException(MealTestData.USER_MEAL_ID, UserTestData.NOT_FOUND);
    }

    @Test
    public void update_NoSuchMeal_ThrowNotFound() {
        checkUpdateMealByIdAndUserIdForException(MealTestData.NOT_FOUND_MEAL_ID, UserTestData.USER_ID);
    }

    private void checkUpdateMealByIdAndUserIdForException(int mealId, int userId) {
        Meal mealToUpdate = MealTestData.getUpdatedUserMeal(mealId, "updated");

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.update(mealToUpdate, userId))
                .withMessage(MealTestData.NOT_FOUND_MSG + mealId);
    }

    @Test
    public void getAll_AllOkForUser_ReturnListWithMeals() {
        List<Meal> meals = mealService.getAll(UserTestData.USER_ID);

        MealTestData.checkMealsListWithSizeContainsSorting(meals, 7, MealTestData.userMeal);
    }

    @Test
    public void getAll_UserWithoutMeals_ReturnEmptyList() {
        List<Meal> meals = mealService.getAll(UserTestData.GUEST_ID);

        Assertions.assertThat(meals).isEmpty();
    }

    @Test
    public void getAll_UserDoesntExists_ReturnEmptyList() {
        List<Meal> meals = mealService.getAll(UserTestData.NOT_FOUND);

        Assertions.assertThat(meals).isEmpty();
    }

    @Test
    public void getBetweenInclusive_AllOkForUser_ReturnListOfMeals() {
        checkGetBetweenInclusiveByUserId(UserTestData.USER_ID, MealTestData.userMeal, 7);
    }

    @Test
    public void getBetweenInclusive_ForAdmin_ReturnOnlyAdminsMeals() {
        checkGetBetweenInclusiveByUserId(UserTestData.ADMIN_ID, MealTestData.adminMeal, 7);
    }

    private void checkGetBetweenInclusiveByUserId(int userId, Meal meal, int size) {
        LocalDate localDate = LocalDateTime.of(2024, 2, 14, 10, 0, 6).toLocalDate();

        List<Meal> meals = mealService.getBetweenInclusive(localDate, localDate.plusDays(2), userId);

        MealTestData.checkMealsListWithSizeContainsSorting(meals, size, meal);
    }

    @Test
    public void getBetweenInclusive_OneDateInBorders_ReturnListOfUserMeals() {
        LocalDate localDate = LocalDateTime.of(2024, 2, 14, 10, 0, 6).toLocalDate();

        List<Meal> betweenInclusive = mealService.getBetweenInclusive(localDate, localDate, UserTestData.USER_ID);

        MealTestData.checkMealsListWithSizeContainsSorting(betweenInclusive, 3, MealTestData.userMeal);
    }

    @Test
    public void getBetweenInclusive_NoMatches_ReturnEmptyList() {
        LocalDate localDate = LocalDateTime.of(2024, 2, 14, 10, 0, 6).toLocalDate();

        List<Meal> meals = mealService.getBetweenInclusive(localDate.minusDays(100), localDate.minusDays(50),
                UserTestData.USER_ID);

        Assertions.assertThat(meals).isEmpty();
    }
}
