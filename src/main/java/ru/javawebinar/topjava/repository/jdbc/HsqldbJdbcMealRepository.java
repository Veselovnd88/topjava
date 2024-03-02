package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
@Profile(Profiles.HSQL_DB)
public class HsqldbJdbcMealRepository extends JdbcMealBaseRepository<Timestamp> {
    public HsqldbJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected void addLocalDateTimeToMeal(MapSqlParameterSource map, LocalDateTime localDateTime) {
        map.addValue("date_time", DateTimeUtil.convertLocalDateTimeToHSQL(localDateTime));
    }

    @Override
    protected Pair<Timestamp, Timestamp> convertTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return Pair.of(Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));
    }
}
