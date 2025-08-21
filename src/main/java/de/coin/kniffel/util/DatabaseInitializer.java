package de.coin.kniffel.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    final static String createPlayer = """
            CREATE TABLE IF NOT EXISTS Player (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL UNIQUE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
            """;

    final static String createGame = """
            CREATE TABLE IF NOT EXISTS Game (
                id INT AUTO_INCREMENT PRIMARY KEY,
                game_nr INT NOT NULL,
                game_year INT NOT NULL,
                date DATE,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
            );
            """;

    final static String createScore = """
            CREATE TABLE IF NOT EXISTS Score (
                game_id INT NOT NULL,
                player_id INT NOT NULL,
                score INT NOT NULL CHECK (score >= 0),
                contribution DOUBLE NOT NULL DEFAULT 0.00,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                PRIMARY KEY (player_id, game_id),
                FOREIGN KEY (player_id) REFERENCES Player(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE,
                FOREIGN KEY (game_id) REFERENCES Game(id)
                    ON DELETE CASCADE
                    ON UPDATE CASCADE
            );
            """;

    public static void initializeDatabase() {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Create players table
            statement.execute(createPlayer);
            statement.execute(createGame);
            statement.execute(createScore);

            System.out.println("Database and table created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
