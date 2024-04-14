package ru.javawebinar.topjava.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.stream.Stream;

public class BadMealArgumentsProvider implements ArgumentsProvider {

    public static final String DATE = "Date/Time";
    public static final String DESCRIPTION = "description";
    public static final String CALORIES = "calories";

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        Meal nullDate = new Meal(1, null, "Завтрак", 500);
        Meal nullDescription = new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), null, 500);
        Meal blankDescription = new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "", 500);
        Meal shortDescription = new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "1", 500);
        Meal longDescription = new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "1".repeat(122), 500);
        Meal nullCalories = new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Description", null);
        Meal lessCalories = new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Description", 9);
        Meal tooMuchCalories = new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Description", 5001);
        return Stream.of(
                Arguments.of(nullDate, DATE),
                Arguments.of(nullDescription, DESCRIPTION),
                Arguments.of(blankDescription, DESCRIPTION),
                Arguments.of(shortDescription, DESCRIPTION),
                Arguments.of(longDescription, DESCRIPTION),
                Arguments.of(nullCalories, CALORIES),
                Arguments.of(lessCalories, CALORIES),
                Arguments.of(tooMuchCalories, CALORIES)
        );
    }
}
