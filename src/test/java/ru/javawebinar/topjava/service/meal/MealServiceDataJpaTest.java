package ru.javawebinar.topjava.service.meal;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJpaTest extends MealServiceBaseTest {

    private static final Logger log = LoggerFactory.getLogger(MealServiceDataJpaTest.class);

    @BeforeClass
    public static void setUp() {
        log.info("Tests for MealService with DataJPA repository launched");
    }
}
