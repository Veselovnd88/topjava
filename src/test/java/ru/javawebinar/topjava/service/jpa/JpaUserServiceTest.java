package ru.javawebinar.topjava.service.jpa;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;


@ActiveProfiles(Profiles.JPA)
public class JpaUserServiceTest extends AbstractUserServiceTest {

    @BeforeClass
    public static void setUp() {
        testLog.info("Tests for UserService with Jpa repository launched");
    }
}
