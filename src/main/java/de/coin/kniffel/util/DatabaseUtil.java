package de.coin.kniffel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseUtil {
    private static final String DB_URL = "jdbc:h2:file:./data/playerdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    /**
     * Returns a Connection object from the centralized database information.
     *
     * @return A Connection object to the database.
     * @throws SQLException if a connection error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static void cleanDatabase() {
        try (Connection connection = getConnection()) {
            connection.createStatement().execute("DELETE FROM Player");
            connection.createStatement().execute("DELETE FROM Game");
            connection.createStatement().execute("DELETE FROM Score");
        } catch (SQLException e) {
            log.error("Error while cleaning database: {}", e.getMessage());
        }
    }

}
