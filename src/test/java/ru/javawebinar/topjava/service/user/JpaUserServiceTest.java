package ru.javawebinar.topjava.service.user;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;


@ActiveProfiles(Profiles.JPA)
public class JpaUserServiceTest extends AbstractUserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(JpaUserServiceTest.class);

    @BeforeClass
    public static void setUp() {
        log.info("Tests for UserService with Jpa repository launched");
    }
}
