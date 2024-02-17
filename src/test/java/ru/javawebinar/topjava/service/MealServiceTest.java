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

import java.util.Comparator;
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

        Meal savedMeal = mealService.create(MealTestData.getNewUserMeal(userId), userId);

        Meal newMealForCheck = MealTestData.getNewUserMeal(userId);
        Integer mealId = savedMeal.getId();
        newMealForCheck.setId(mealId);
        MealTestData.assertMatch(savedMeal, newMealForCheck);
        MealTestData.assertMatch(mealService.get(mealId, userId), savedMeal);
    }

    @Test
    public void create_SameDateTimeSameUser_ThrowException() {
        int userId = UserTestData.USER_ID;
        Meal newMeal = MealTestData.getNewUserMeal(userId);
        newMeal.setDateTime(MealTestData.LOCAL_DATE_TIME);
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

    @Test
    public void get_ForExistingGuestMeal_ReturnCorrectMeal() {
        checkGetMealByIdAndUserId(MealTestData.GUEST_MEAL_ID, UserTestData.GUEST_ID, MealTestData.guestMeal);
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
        checkDeleteMealByIdAndUserId(MealTestData.USER_MEAL_ID, UserTestData.USER_ID);
    }

    @Test
    public void delete_AllOkForAdmin_Delete() {
        checkDeleteMealByIdAndUserId(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);
    }

    @Test
    public void delete_AllOkForGuest_Delete() {
        checkDeleteMealByIdAndUserId(MealTestData.GUEST_MEAL_ID, UserTestData.GUEST_ID);
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
    public void update_AllOkForUser_UpdateUserMeal() {
        checkUpdateMealByIdAndUserId(MealTestData.USER_MEAL_ID, UserTestData.USER_ID);
    }

    @Test
    public void update_AllOkForAdmin_UpdateAdminMeal() {
        checkUpdateMealByIdAndUserId(MealTestData.ADMIN_MEAL_ID, UserTestData.ADMIN_ID);
    }

    @Test
    public void update_AllOkForGuest_UpdateGuestMeal() {
        checkUpdateMealByIdAndUserId(MealTestData.GUEST_MEAL_ID, UserTestData.GUEST_ID);
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
    public void getAll_AllOkForUser_ReturnListWithMeals() {
        List<Meal> meals = mealService.getAll(UserTestData.USER_ID);

        Assertions.assertThat(meals).hasSize(7).contains(MealTestData.userMeal)
                .isSortedAccordingTo(Comparator.comparing(Meal::getDateTime).reversed());
    }

    @Test
    public void getAll_UserWithoutMeals_ReturnEmptyList() {
        List<Meal> meals = mealService.getAll(UserTestData.NOT_FOUND);

        Assertions.assertThat(meals).isEmpty();
    }

    @Test
    public void getBetweenInclusive_AllOkForUser_ReturnListOfMeals() {
        checkGetBetweenInclusiveByUserId(UserTestData.USER_ID, MealTestData.userMeal);
    }

    @Test
    public void getBetweenInclusive_ForAdmin_ReturnOnlyAdminsMeals() {
        checkGetBetweenInclusiveByUserId(UserTestData.ADMIN_ID, MealTestData.adminMeal);
    }

    @Test
    public void getBetweenInclusive_ForGuest_ReturnOnlyAdminsMeals() {
        checkGetBetweenInclusiveByUserId(UserTestData.GUEST_ID, MealTestData.guestMeal);
    }

    @Test
    public void getBetweenInclusive_OneDateInBorders_ReturnListOfUserMeals() {
        List<Meal> betweenInclusive = mealService.getBetweenInclusive(MealTestData.LOCAL_DATE_TIME.toLocalDate(),
                MealTestData.LOCAL_DATE_TIME.toLocalDate(), UserTestData.USER_ID);
        Assertions.assertThat(betweenInclusive).hasSize(3).contains(MealTestData.userMeal);
        checkMealSortingOrder(betweenInclusive);
    }

    @Test
    public void getBetweenInclusive_NoMatches_ReturnEmptyList() {
        List<Meal> betweenInclusive = mealService.getBetweenInclusive(MealTestData.LOCAL_DATE_TIME.toLocalDate().minusDays(100),
                MealTestData.LOCAL_DATE_TIME.toLocalDate().minusDays(50), UserTestData.USER_ID);

        Assertions.assertThat(betweenInclusive).isEmpty();
    }

    private void checkGetMealByIdAndUserId(int mealId, int userId, Meal meal) {
        Meal foundMeal = mealService.get(mealId, userId);

        MealTestData.assertMatch(foundMeal, meal);
    }

    private void checkDeleteMealByIdAndUserId(int mealId, int userId) {
        mealService.delete(mealId, userId);

        Assertions.assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> mealService.get(mealId, userId))
                .withMessage("Not found entity with id=" + mealId);
    }

    private void checkUpdateMealByIdAndUserId(int mealId, int userId) {
        Meal mealToUpdate = MealTestData.getUpdatedUserMeal(mealId, userId, "updated");

        mealService.update(mealToUpdate, userId);

        Meal updatedMeal = mealService.get(mealId, userId);
        MealTestData.assertMatch(updatedMeal, mealToUpdate);
    }

    private void checkGetBetweenInclusiveByUserId(int userId, Meal meal) {
        List<Meal> betweenInclusive = mealService.getBetweenInclusive(MealTestData.LOCAL_DATE_TIME.toLocalDate(),
                MealTestData.LOCAL_DATE_TIME.toLocalDate().plusDays(2), userId);

        Assertions.assertThat(betweenInclusive).hasSize(7).contains(meal);
        checkMealSortingOrder(betweenInclusive);
    }

    private void checkMealSortingOrder(List<Meal> meals) {
        Assertions.assertThat(meals).isSortedAccordingTo(Comparator.comparing(Meal::getDateTime).reversed());
    }
}
