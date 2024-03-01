package ru.javawebinar.topjava.repository.jdbc;

import java.time.LocalDateTime;

public interface JdbcHelper<T> {

    T convertLocalDateTime(LocalDateTime localDateTime);
}
