package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile(Profiles.HSQL_DB)
public class JdbcMealHSQLDBRepository extends JdbcMealBaseRepository {
    public JdbcMealHSQLDBRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return super.getJdbcTemplate().query(
                "SELECT * FROM meal WHERE user_id=?  AND date_time >=  ? AND date_time < ? ORDER BY date_time DESC",
                ROW_MAPPER, userId,
                DateTimeUtil.convertLocalDateTimeToHSQL(startDateTime),
                DateTimeUtil.convertLocalDateTimeToHSQL(endDateTime));
    }

    @Override
    protected void addLocalDateTimeToMeal(MapSqlParameterSource map, LocalDateTime localDateTime) {
        map.addValue("date_time", DateTimeUtil.convertLocalDateTimeToHSQL(localDateTime));
    }
}
