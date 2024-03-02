package ru.javawebinar.topjava.service.jpa;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

@ActiveProfiles(Profiles.JPA)
public class JpaMealServiceTest extends AbstractMealServiceTest {

    @BeforeClass
    public static void setUp() {
        testLog.info("Tests for MealService with JPA repository launched");
    }
}
