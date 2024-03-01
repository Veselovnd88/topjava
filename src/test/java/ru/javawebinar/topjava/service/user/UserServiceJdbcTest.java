package ru.javawebinar.topjava.service.user;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceBaseTest;

@ActiveProfiles(Profiles.JDBC)
public class UserServiceJdbcTest extends UserServiceBaseTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceJdbcTest.class);

    @BeforeClass
    public static void setUp() {
        log.info("Tests for UserService with Jdbc launched");
    }
}
