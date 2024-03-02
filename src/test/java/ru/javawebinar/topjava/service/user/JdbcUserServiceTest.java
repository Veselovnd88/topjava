package ru.javawebinar.topjava.service.user;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

@ActiveProfiles(Profiles.JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(JdbcUserServiceTest.class);

    @BeforeClass
    public static void setUp() {
        log.info("Tests for UserService with Jdbc launched");
    }
}
