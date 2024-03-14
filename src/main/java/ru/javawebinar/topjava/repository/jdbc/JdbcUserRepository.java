package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final SimpleJdbcInsert insertRole;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.insertRole = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_role")
                .usingColumns("user_id", "role");
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            saveRoles(user.getRoles().stream().toList(), newKey.intValue());
            user.setId(newKey.intValue());
            return user;
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) != 0) {
            deleteRoles(user.id());
            saveRoles(user.getRoles().stream().toList(), user.id());
            return user;
        }
        return null;
    }

    private void saveRoles(List<Role> roles, int userId) {
        jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Role role = roles.get(i);
                        ps.setInt(1, userId);
                        ps.setString(2, role.name());
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        boolean deleted = jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
        if (deleted) {
            deleteRoles(id);
        }
        return deleted;
    }

    private void deleteRoles(int userId) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", userId);
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ON users.id = user_role.user_id WHERE id=?",
                getResultSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate
                .query("SELECT * FROM users LEFT JOIN user_role ON users.id = user_role.user_id WHERE email=?", getResultSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate
                .query("SELECT * FROM users LEFT JOIN user_role ON users.id = user_role.user_id ORDER BY name, email", getResultSetExtractor());
    }

    private ResultSetExtractor<List<User>> getResultSetExtractor() {
        return rs -> {
            Map<Integer, User> userMap = new LinkedHashMap<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRegistered(rs.getDate("registered"));
                user.setPassword(rs.getString("password"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                String role = rs.getString("role");
                user.setRoles(role == null ? Collections.emptySet() : Collections.singleton(Role.valueOf(role)));
                userMap.merge(user.getId(), user, (oldValue, value) -> {
                    if (role != null) {
                        oldValue.getRoles().add(Role.valueOf(role));
                    }
                    return oldValue;
                });
            }
            return userMap.values().stream().toList();
        };
    }
}
