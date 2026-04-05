package de.coin.kniffel.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    public static String formatDate(LocalDate date) {
        return DATE_FORMATTER.format(date);
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return false;
        }
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
