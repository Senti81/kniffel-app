package de.coin.kniffel.util;

import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy@HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String getCurrentDate(boolean withSeconds) {
        return withSeconds ? DATE_TIME_FORMATTER.format(java.time.LocalDateTime.now()) :
                DATE_FORMATTER.format(java.time.LocalDateTime.now());
    }

    public static String getCurrentDate() {
        return getCurrentDate(false);
    }
}
