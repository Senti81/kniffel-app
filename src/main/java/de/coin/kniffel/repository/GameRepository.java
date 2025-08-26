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

import de.coin.kniffel.model.Game;
import de.coin.kniffel.model.dto.GameDTO;
import de.coin.kniffel.util.DatabaseUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameRepository {

    private static final String FIND_ALL_GAMES = "SELECT * FROM Game";
    private static final String GET_ALL_GAME_YEARS = "SELECT DISTINCT (game_year) FROM GAME";
    private static final String GET_ALL_GAME_NUMBER_BY_YEAR = "SELECT game_nr FROM GAME WHERE game_year = ?";

    private static final String FIND_BY_NUMBER_AND_YEAR = "SELECT * FROM GAME WHERE game_nr = ? AND game_year = ?";
//    private static final String FIND_LATEST_GAME = """
//                        SELECT * FROM Game WHERE GAME_NR = (
//                        SELECT MAX(GAME_NR) FROM GAME WHERE GAME_YEAR = ?);
//                        """;
    private static final String FIND_LATEST_GAME = "SELECT * FROM Game WHERE Id = (SELECT MAX(Id) FROM Game)";
    private static final String INSERT_GAME = "INSERT INTO Game (game_nr, game_year, date, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

    public int save(Game game) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GAME, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, game.getNr());
            preparedStatement.setInt(2, game.getYear());
            preparedStatement.setDate(3, Date.valueOf(game.getDate()));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

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

    public List<Integer> getAllGameYears() {
        List<Integer> gameYears = new ArrayList<>();
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_GAME_YEARS);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int year = resultSet.getInt("game_year");
                gameYears.add(year);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameYears;
    }

    public List<Integer> getAllGameNumbersByYear(int year) {
        List<Integer> gameNumbers = new ArrayList<>();
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_ALL_GAME_NUMBER_BY_YEAR);
            statement.setInt(1, year);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int gameNumber = resultSet.getInt("game_nr");
                gameNumbers.add(gameNumber);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameNumbers;
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

                GameDTO gameDTO = new GameDTO();
                gameDTO.setGameNumber(gameNumber);
                gameDTO.setGameYear(gameYear);
                gameDTO.setGameDate(gameDate);
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

    public GameDTO findLatestGame() {
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_LATEST_GAME);

//            statement.setInt(1, LocalDate.now().getYear());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                GameDTO gameDTO = new GameDTO();
                gameDTO.setGameNumber(result.getInt("game_nr"));
                gameDTO.setGameYear(result.getInt("game_year"));
                gameDTO.setGameDate(result.getDate("date").toLocalDate());
                log.info("Found game: {}", gameDTO);
                return gameDTO;
            } else {
                log.info("No games found for this year.");
                return null;
            }
        } catch (SQLException e) {
            log.error("Error while fetching game by number: {}", e.getMessage());
            return null;
        }
    }

}
