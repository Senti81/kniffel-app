package de.coin.kniffel.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImportExportUtil {

    private static final String PLAYER_FILE_PATH = "data/PlayerData.csv";
    private static final String GAME_FILE_PATH = "data/GameData.csv";
    private static final String SCORE_FILE_PATH = "data/ScoreData.csv";

    public static boolean exportDatabaseToCSV() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            exportTableToCSV(connection, "Player", PLAYER_FILE_PATH);
            exportTableToCSV(connection, "Game", GAME_FILE_PATH);
            exportTableToCSV(connection, "Score", SCORE_FILE_PATH);
        } catch (SQLException | IOException e) {
            log.error("Error while exporting database to CSV: {}", e.getMessage(), e);
            return false;
        }
        return true;
    }

    public static void importCSVToDatabase() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            importCSVToTable(connection, PLAYER_FILE_PATH, "Player",
                    "INSERT INTO Player (id, name, created_at, updated_at) VALUES (?, ?, ?, ?)");
            importCSVToTable(connection, GAME_FILE_PATH, "Game",
                    "INSERT INTO Game (id, game_nr, game_year, date, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)");
            importCSVToTable(connection, SCORE_FILE_PATH, "Score",
                    "INSERT INTO Score (game_id, player_id, score, contribution, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)");
        } catch (SQLException | IOException e) {
            log.error("Error while importing CSV to database: {}", e.getMessage(), e);
        }
    }

    private static void exportTableToCSV(Connection connection, String tableName, String filePath) throws SQLException, IOException {
        String query = "SELECT * FROM " + tableName;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
             FileWriter writer = new FileWriter(filePath)) {

            int columnCount = resultSet.getMetaData().getColumnCount();

            // Write the header line (column names)
            for (int i = 1; i <= columnCount; i++) {
                writer.append(resultSet.getMetaData().getColumnName(i));
                if (i < columnCount) writer.append(",");
            }
            writer.append("\n");

            // Write the data rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    writer.append(value != null ? value.toString() : "NULL");
                    if (i < columnCount) writer.append(",");
                }
                writer.append("\n");
            }

            log.info("Exported table '{}' to '{}'", tableName, filePath);
        }
    }

    private static void importCSVToTable(Connection connection, String filePath, String tableName, String insertQuery) throws SQLException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            String line = reader.readLine(); // Skip the header line
            if (line == null) {
                log.warn("The file '{}' is empty. Skipping import for table '{}'.", filePath, tableName);
                return;
            }

            // Parse the rest of the lines
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                // Fill the prepared statement with values from the file
                for (int i = 0; i < values.length; i++) {
                    String value = values[i].trim();
                    if (value.equalsIgnoreCase("NULL") || value.isEmpty()) {
                        preparedStatement.setObject(i + 1, null); // Bind NULL values
                    } else {
                        preparedStatement.setString(i + 1, value); // Bind actual values
                    }
                }

                preparedStatement.addBatch(); // Add to batch
            }

            preparedStatement.executeBatch(); // Execute batch insert
            log.info("Imported data from '{}' into table '{}'", filePath, tableName);

        } catch (IOException | SQLException e) {
            log.error("Error while importing file '{}' into table '{}': {}", filePath, tableName, e.getMessage());
            throw e; // Rethrow to handle it higher up
        }
    }

}
