package de.coin.kniffel.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.coin.kniffel.model.Score;
import de.coin.kniffel.model.dto.GameResultDTO;
import de.coin.kniffel.model.dto.ResultDTO;
import de.coin.kniffel.util.DatabaseUtil;

public class ScoreRepository {

    private static final Logger log = LoggerFactory.getLogger(ScoreRepository.class);

    private static final String INSERT_SCORE = "INSERT INTO Score (game_id, player_id, score) VALUES (?, ?, ?)";
    private static final String UPDATE_CONTRIBUTION = "UPDATE Score SET contribution = ? WHERE game_id = ? AND player_id = ?";

    private static final String GET_RESULT_BY_GAME_ID = """
            SELECT      GAME_ID, PLAYER_ID, SCORE
            FROM        Score
            WHERE       GAME_ID = ?
            ORDER BY    SCORE DESC;
            """;

    private static final String GET_RESULTS_OF_EACH_PLAYER_BY_YEAR_AND_GAME = """
            SELECT      p.name, s.score, s.contribution
            FROM        Score s
            JOIN        Game g on s.GAME_ID = g.ID
            JOIN        Player p on s.PLAYER_ID = p.ID
            WHERE       game_year = ? AND game_nr = ?
            GROUP BY    p.name, s.score
            ORDER BY    s.score DESC;
            """;

    private static final String GET_SEASON_RESULTS_BY_YEAR = """
            SELECT      p.NAME, SUM(s.score) AS total_score, SUM(s.contribution) AS total_contribution
            FROM        Score s
            JOIN        Game g on s.GAME_ID = g.ID
            JOIN        Player p on s.PLAYER_ID = p.ID
            WHERE       GAME_YEAR = ?
            GROUP BY    p.NAME
            ORDER BY    total_score DESC;
            """;


    // for debug only
    private static final String QUERY = """
            SELECT
                g.game_nr AS game_number,
                g.game_year AS game_year,
                g.date AS game_date,
                p.name AS player_name,
                s.score AS final_score
            FROM Score s
            JOIN Player p ON s.PLAYER_ID = p.ID
            JOIN Game g ON s.GAME_ID = g.ID
        """;

    public void save(Score score) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_SCORE);

            statement.setInt(1, score.getGameId());
            statement.setInt(2, score.getPlayerId());
            statement.setInt(3, score.getFinalScore());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                log.info("Score saved successfully with ID: {}", generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            log.error("Error while saving score: {}", e.getMessage());
        }
    }

    public List<GameResultDTO> getScoreOfEachPlayerByYearAndGame(int year, int gameNumber) {
        log.info("Fetching results of each player by year {} and game {}", year, gameNumber);
        List<GameResultDTO> gameResultDTOList = new ArrayList<>();

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_RESULTS_OF_EACH_PLAYER_BY_YEAR_AND_GAME);

            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, gameNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            int index = 0;
            while (resultSet.next()) {
                index++;
                int position = index;
                String playerName = resultSet.getString("name");
                int finalScore = resultSet.getInt("score");
                double contribution = resultSet.getDouble("contribution");

                log.info("Found player {} with final score {} - Contribution: {}", playerName, finalScore, contribution);
                GameResultDTO gameResultDTO = new GameResultDTO();
                gameResultDTO.getPosition().set(position);
                gameResultDTO.getPlayerName().set(playerName);
                gameResultDTO.getFinalScore().set(finalScore);
                gameResultDTO.getContribution().set(contribution);
                gameResultDTOList.add(gameResultDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameResultDTOList;
    }

    public List<GameResultDTO> getSeasonResultsByYear(int year) {
        List<GameResultDTO> gameResultDTOList = new ArrayList<>();

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_SEASON_RESULTS_BY_YEAR);

            preparedStatement.setInt(1, year);
            ResultSet resultSet = preparedStatement.executeQuery();

            int index = 0;
            while (resultSet.next()) {
                index++;
                int position = index;
                String playerName = resultSet.getString("name");
                int totalScore = resultSet.getInt("total_score");
                double contribution = resultSet.getDouble("total_contribution");
                GameResultDTO gameResultDTO = new GameResultDTO();
                gameResultDTO.getPosition().set(position);
                gameResultDTO.getPlayerName().set(playerName);
                gameResultDTO.getFinalScore().set(totalScore);
                gameResultDTO.getContribution().set(contribution);

                log.info("Found player {} with final score {} - Contribution: {}", playerName, totalScore, contribution);
                gameResultDTOList.add(gameResultDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return gameResultDTOList;
    }

    public List<ResultDTO> getResultsByGameId(int gameId) {
        List<ResultDTO> resultDTOList = new ArrayList<>();
        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(GET_RESULT_BY_GAME_ID);
            statement.setInt(1, gameId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ResultDTO resultDTO = new ResultDTO();
                resultDTO.setGameId(resultSet.getInt("game_id"));
                resultDTO.setPlayerId(resultSet.getInt("player_id"));
                resultDTO.setScore(resultSet.getInt("score"));
                resultDTOList.add(resultDTO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultDTOList;
    }

    public void updateContribution(int gameId, int playerId, double contribution) {
        try {
            log.info("Updating contribution for game {} and player {} with value {}", gameId, playerId, contribution);
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_CONTRIBUTION);
            statement.setDouble(1, contribution);
            statement.setInt(2, gameId);
            statement.setInt(3, playerId);

            statement.executeUpdate();
            log.info("Contribution for game {} and player {} updated successfully", gameId, playerId);
        } catch (SQLException e) {
            log.error("Error while updating score: {}", e.getMessage());
        }
    }

}
