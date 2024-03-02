package ru.javawebinar.topjava.service.meal;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {

    private static final Logger log = LoggerFactory.getLogger(JdbcMealServiceTest.class);

    @BeforeClass
    public static void setUp() {
        log.info("Tests for MealService with Jdbc launched");
    }
}
