package ru.javawebinar.topjava.service.jdbc;

import org.junit.BeforeClass;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

@ActiveProfiles(Profiles.JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    @BeforeClass
    public static void setUp() {
        testLog.info("Tests for UserService with Jdbc launched");
    }
}
