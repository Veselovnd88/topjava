package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.Profiles;

import java.time.LocalDateTime;

@Profile(Profiles.POSTGRES_DB)
@Component
public class JdbcPostgresHelper implements JdbcHelper<LocalDateTime> {
    @Override
    public LocalDateTime convertLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime;
    }
}
