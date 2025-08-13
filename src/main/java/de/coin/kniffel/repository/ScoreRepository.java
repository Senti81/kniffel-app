package de.coin.kniffel.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.coin.kniffel.model.Score;
import de.coin.kniffel.model.dto.ScoreDTO;
import de.coin.kniffel.model.dto.TotalPlayerScoreDTO;
import de.coin.kniffel.util.DatabaseUtil;

public class ScoreRepository {

    private static final Logger log = LoggerFactory.getLogger(ScoreRepository.class);

    private static final String INSERT_SCORE = "INSERT INTO Score (game_id, player_id, score) VALUES (?, ?, ?)";
    private static final String GET_TOTAL_SCORE_OF_EACH_PLAYER_BY_YEAR = """
            SELECT      p.NAME, SUM(s.score) AS total_score
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

    public List<TotalPlayerScoreDTO> getTotalScoreOfEachPlayerByYear(int year) {
        List<TotalPlayerScoreDTO> totalPlayerScoreDTOList = new ArrayList<>();

        try {
            Connection connection = DatabaseUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TOTAL_SCORE_OF_EACH_PLAYER_BY_YEAR);

             preparedStatement.setInt(1, year);
             ResultSet resultSet = preparedStatement.executeQuery();

            int index = 0;
            while (resultSet.next()) {
                index++;
                int position = index;
                String playerName = resultSet.getString("name");
                int totalScore = resultSet.getInt("total_score");
                TotalPlayerScoreDTO totalPlayerScoreDTO = new TotalPlayerScoreDTO(position, playerName, totalScore);
                totalPlayerScoreDTOList.add(totalPlayerScoreDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalPlayerScoreDTOList;
    }

}
