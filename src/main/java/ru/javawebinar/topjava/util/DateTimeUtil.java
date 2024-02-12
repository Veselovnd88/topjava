package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T source, T left, T right) {
        return source.compareTo(left) >= 0 && source.compareTo(right) < 0;
    }

    public static LocalDateTime mapLocalDateToDateWithRightBorder(LocalDate localDate) {
        if (localDate == null || localDate.equals(LocalDate.MAX)) {
            return LocalDateTime.MAX;
        }
        return localDate.plusDays(1).atStartOfDay();
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

