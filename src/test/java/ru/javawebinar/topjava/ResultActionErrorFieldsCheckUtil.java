package ru.javawebinar.topjava;

import org.hamcrest.Matchers;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javawebinar.topjava.util.exception.ErrorType;

public class ResultActionErrorFieldsCheckUtil {

    public static final String JSON_URL = "$.url";

    public static final String JSON_TYPE = "$.type";

    public static final String JSON_DETAIL = "$.detail";

    public static void checkValidationErrorFields(ResultActions resultActions, String url, String fieldName) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath(JSON_URL, Matchers.containsString(url)))
                .andExpect(MockMvcResultMatchers.jsonPath(JSON_TYPE).value(ErrorType.VALIDATION_ERROR.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath(JSON_DETAIL, Matchers.containsStringIgnoringCase(fieldName)));
    }
}
