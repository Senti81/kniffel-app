package de.coin.kniffel.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.model.dto.GameDTO;
import de.coin.kniffel.util.DatabaseUtil;

public class GameRepository {

    private static final Logger log = LoggerFactory.getLogger(GameRepository.class);

    private static final String FIND_ALL_GAMES = "SELECT * FROM Game";
    private static final String UPDATE_GAME = "UPDATE Game SET game_nr = ?, date = ?, updated_at = ? WHERE id = ?";
    private static final String FIND_BY_NUMBER_AND_YEAR = "SELECT * FROM GAME WHERE game_nr = ? AND game_year = ?";
    private static final String FIND_LATEST_GAME_NR = "SELECT MAX(GAME_NR) as latest_game_nr FROM GAME WHERE GAME_YEAR = ?";
    private static final String INSERT_GAME = "INSERT INTO Game (game_nr, game_year, date, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

    public int save(Game game) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GAME, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, game.getNr());
            preparedStatement.setInt(2, game.getYear());
            preparedStatement.setDate(3, Date.valueOf(game.getDate()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(game.getCreatedAt()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(game.getUpdatedAt()));

            preparedStatement.executeUpdate();

            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                log.info("Game saved successfully with ID: {}", keys.getInt(1));
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            log.error("Error while saving game: {}", e.getMessage());
        }
        return -1;
    }

    public List<GameDTO> findAll() {
        List<GameDTO> games = new ArrayList<>();
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_GAMES);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int gameNumber = resultSet.getInt("game_nr");
                int gameYear = resultSet.getInt("game_year");
                LocalDate gameDate = resultSet.getDate("date").toLocalDate();

                GameDTO gameDTO = new GameDTO(gameNumber, gameYear, gameDate);
                games.add(gameDTO);
            }

        } catch (SQLException e) {
            log.error("Error while fetching all games: {}", e.getMessage());
        }
        log.info("Found {} entries", games.size());
        return games;
    }

    public Game findByGameNumberAndYear(int gameNumber, int gameYear) {
        log.info("Fetching game by game number and year: {}, {}", gameNumber, gameYear);
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_NUMBER_AND_YEAR);

            statement.setInt(1, gameNumber);
            statement.setInt(2, gameYear);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                Game game = new Game();
                game.setNr(result.getInt("game_nr"));
                game.setYear(result.getInt("game_year"));
                game.setDate(result.getDate("date").toLocalDate());
                game.setCreatedAt(result.getTimestamp("created_at").toLocalDateTime());
                game.setUpdatedAt(result.getTimestamp("updated_at").toLocalDateTime());
                return game;
            } else {
                log.info("Game not found by game number: {}", gameNumber);
                return null;
            }
        } catch (SQLException e) {
            log.error("Error while fetching game number and year: {}", e.getMessage());
            return null;
        }
    }

    public int findLatestGameNumber() {
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_LATEST_GAME_NR);

            statement.setInt(1, LocalDate.now().getYear());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                log.info("Latest game number: {}", result.getInt("latest_game_nr"));
                return result.getInt("latest_game_nr");
            } else {
                log.info("No games found for this year.");
                return 0;
            }
        } catch (SQLException e) {
            log.error("Error while fetching game by number: {}", e.getMessage());
            return -1;
        }
    }

    public void update(Game game) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_GAME);

            preparedStatement.setInt(1, game.getNr());
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(1, String.valueOf(game.getDate()));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("Error while updating game: {}", e.getMessage());
        }

    }

}
