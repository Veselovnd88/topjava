package ru.javawebinar.topjava.exception;

public class ExceptionUtils {

    public static final String MEAL_NOT_FOUND = "Meal with [id: %s] not found";

    private ExceptionUtils() {
        throw new AssertionError("Not instances for util class");
    }

}
