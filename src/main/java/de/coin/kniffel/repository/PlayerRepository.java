package de.coin.kniffel.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.coin.kniffel.model.Player;
import de.coin.kniffel.util.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerRepository{

    // --- SQL Queries ---
    private static final String SAVE_PLAYER = "INSERT INTO Player (name) VALUES (?)";
    private static final String READ_ALL_PLAYERS = "SELECT * FROM Player";
    private static final String UPDATE_PLAYER = "UPDATE Player SET name = ? WHERE id = ?";
    private static final String DELETE_PLAYER = "DELETE FROM Player WHERE id = ?";

    /**
     * Saves a new player record into the database.
     * Utilizes {@link DatabaseUtil#getConnection()} to establish a database connection.
     *
     * @param player the Player object containing the data to be saved, including
     *               the name, creation timestamp, and update timestamp.
     */
    public void save(Player player) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_PLAYER, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, player.getPlayerName());
            preparedStatement.executeUpdate();

            log.info("Player {} saved successfully", player.getPlayerName());

        } catch (SQLException e) {
            log.error("Error while saving player: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<Player> findAll() {
        List<Player> players = new ArrayList<>();

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(READ_ALL_PLAYERS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Player player = new Player();
                player.setPlayerId(resultSet.getInt("id"));
                player.setPlayerName(resultSet.getString("name"));
                player.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                player.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());

                players.add(player);
            }

        } catch (SQLException e) {
            log.error("Error while fetching all players: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        return players;
    }

    public void update(Player player) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PLAYER);

            preparedStatement.setString(1, player.getPlayerName());
            preparedStatement.setInt(2, player.getPlayerId());
            preparedStatement.executeUpdate();

            log.info("Player {} updated successfully", player.getPlayerName());
        } catch (SQLException e) {
            log.error("Error while updating player: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(Player player) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PLAYER);

            preparedStatement.setInt(1, player.getPlayerId());
            preparedStatement.executeUpdate();

            log.info("Player {} deleted successfully", player.getPlayerName());
        } catch (SQLException e) {
            log.error("Error while deleting player: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
