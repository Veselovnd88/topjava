package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;

@Profile(Profiles.HSQL_DB)
@Component
public class JdbcHSQLHelper implements JdbcHelper<String> {
    @Override
    public String convertLocalDateTime(LocalDateTime localDateTime) {
        return DateTimeUtil.convertLocalDateTimeToHSQL(localDateTime);
    }
}
