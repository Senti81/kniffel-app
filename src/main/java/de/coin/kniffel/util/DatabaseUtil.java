package de.coin.kniffel.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

}
