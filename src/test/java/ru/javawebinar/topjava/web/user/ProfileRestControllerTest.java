package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.ResultActionErrorFieldsCheckUtil;
import ru.javawebinar.topjava.extension.InvalidUserArgumentsProvider;
import ru.javawebinar.topjava.extension.annotation.NullPasswordUser;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UsersUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;
import static ru.javawebinar.topjava.UserTestData.USER_WITH_MEALS_MATCHER;
import static ru.javawebinar.topjava.UserTestData.admin;
import static ru.javawebinar.topjava.UserTestData.guest;
import static ru.javawebinar.topjava.UserTestData.user;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.getAll(), admin, guest);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);
        User newUser = UsersUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "user@yandex.ru", "newPassword", 1500);
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), UsersUtil.updateFromTo(new User(user), updatedTo));
    }

    @Test
    void getWithMeals() throws Exception {
        assumeDataJpa();
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-meals")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_WITH_MEALS_MATCHER.contentJson(user));
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidUserArgumentsProvider.class)
    void register_ValidationFailed_ReturnError(User user, String field) throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(UsersUtil.asTo(user))))
                .andExpect(status().isUnprocessableEntity());
        ResultActionErrorFieldsCheckUtil.checkValidationErrorFields(resultActions, REST_URL, field);
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidUserArgumentsProvider.class)
    void update_ValidationFailed_ReturnError(User badUser, String field) throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(UsersUtil.asTo(badUser))))
                .andExpect(status().isUnprocessableEntity());
        ResultActionErrorFieldsCheckUtil.checkValidationErrorFields(resultActions, REST_URL, field);
    }

    @Test
    void register_ValidationFailedWithNullPassword_ReturnError(@NullPasswordUser User badUser) throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(UsersUtil.asTo(badUser))))
                .andExpect(status().isUnprocessableEntity());
        ResultActionErrorFieldsCheckUtil.checkValidationErrorFields(resultActions, REST_URL, "password");
    }

    @Test
    void update_ValidationFailedWithNullPassword_ReturnError(@NullPasswordUser User badUser) throws Exception {
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(UsersUtil.asTo(badUser))))
                .andExpect(status().isUnprocessableEntity());
        ResultActionErrorFieldsCheckUtil.checkValidationErrorFields(resultActions, REST_URL, "password");
    }
}
