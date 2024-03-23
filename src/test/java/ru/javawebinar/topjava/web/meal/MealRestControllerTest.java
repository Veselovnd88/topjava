package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

class MealRestControllerTest extends AbstractControllerTest {

    private static String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    MealService mealService;

    @Test
    void createWithLocation_AllOk_ReturnCreatedMealWitLocation() throws Exception {
        Meal meal = MealTestData.getNew();

        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(meal)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Meal created = MealTestData.MEAL_MATCHER.readFromJson(resultActions);
        int newId = created.id();
        meal.setId(newId);
        MealTestData.MEAL_MATCHER.assertMatch(created, meal);
        MealTestData.MEAL_MATCHER.assertMatch(created, mealService.get(newId, SecurityUtil.authUserId()));
    }

    @Test
    void get_AllOk_ReturnMeal() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MealTestData.MEAL1_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEAL_MATCHER.contentJson(MealTestData.meal1));
    }

    @Test
    void getAll_AllOK_ReturnListOfMealTos() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEALTO_MATCHER.contentJson(MealTestData.mealTos));
    }

    @Test
    void getBetween_AllOk_ReturnListOfFilteredMealTos() throws Exception {
        LocalDate date = LocalDate.of(2020, Month.JANUARY, 30);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(20, 0);
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter")
                .param("startDate", String.valueOf(date))
                .param("startTime", String.valueOf(startTime))
                .param("endDate", String.valueOf(date))
                .param("endTime", String.valueOf(endTime)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MEALTO_MATCHER.contentJson(
                        MealsUtil.getFilteredTos(List.of(MealTestData.meal3, MealTestData.meal2, MealTestData.meal1),
                                MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime)));
    }

    @Test
    void update_AllOk_UpdateAndReturnOk() throws Exception {
        Meal meal = MealTestData.getNew();

        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(meal)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Meal created = MealTestData.MEAL_MATCHER.readFromJson(resultActions);
        int newId = created.id();
        meal.setId(newId);
        MealTestData.MEAL_MATCHER.assertMatch(created, meal);
        MealTestData.MEAL_MATCHER.assertMatch(created, mealService.get(newId, SecurityUtil.authUserId()));
    }

}