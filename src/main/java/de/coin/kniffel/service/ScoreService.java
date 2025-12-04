package de.coin.kniffel.service;

import java.util.List;

import de.coin.kniffel.model.Score;
import de.coin.kniffel.model.dto.GameResultDTO;
import de.coin.kniffel.model.dto.ResultDTO;
import de.coin.kniffel.repository.ScoreRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScoreService {

    private static final List<Double> DEFAULT_CONTRIBUTION = List.of(3.50D, 4.00D, 4.50D, 5.00D);

    private static final double PENALTY_LOW_SCORE = 3.00D;
    private static final double PENALTY_HIGH_SCORE = 1.00D;
    private static final int MIN_SCORE_THRESHOLD = 800;
    private static final int MAX_SCORE_THRESHOLD = 1300;

    private final ScoreRepository scoreRepository;

    public ScoreService() {
        scoreRepository = new ScoreRepository();
    }

    public void saveScore(Score score) {
        scoreRepository.save(score);
    }

    public List<GameResultDTO> getSeasonResultsByYear(int year) {
        return scoreRepository.getSeasonResultsByYear(year);
    }

    public List<GameResultDTO> getGameResultsByYearAndGameNumber(int year, int gameNumber) {
        return scoreRepository.getScoreOfEachPlayerByYearAndGame(year, gameNumber);
    }

    public void getResultsByGameId(int gameId) {
        List<ResultDTO> results = scoreRepository.getResultsByGameId(gameId);

        List<ResultDTO> updatedResults = calculateContributions(results);
        for (ResultDTO result : updatedResults) {
            setContribution(gameId, result.getPlayerId(), result.getContribution());
            log.info("Contribution for game {} and player {} updated successfully", gameId, result.getPlayerId());
        }
    }

    private void setContribution(int gameId, int playerId, double contribution) {
        log.info("Setting contribution for game {} and player {} with value {}", gameId, playerId, contribution);
        scoreRepository.updateContribution(gameId, playerId, contribution);
    }

    List<ResultDTO> calculateContributions(List<ResultDTO> results) {
        // Loop through each ResultDTO to calculate individual contribution
        for (ResultDTO result : results) {
            // Calculate initial contribution based on default contribution and position
            double contribution = DEFAULT_CONTRIBUTION.get(results.indexOf(result));

            // Check if the player's score is below the minimum score threshold and apply penalty
            if (result.getScore() < MIN_SCORE_THRESHOLD) {
                contribution += PENALTY_LOW_SCORE;
            }

            // Update the contribution property of the player
            result.setContribution(contribution);
        }

        // Check for players exceeding the maximum score threshold and adjust contributions for others
        for (ResultDTO highScoringPlayer : results) {
            if (highScoringPlayer.getScore() >= MAX_SCORE_THRESHOLD) {
                // High scorer identified, add bonus to all other players
                for (ResultDTO otherPlayer : results) {
                    if (otherPlayer.getPlayerId() != highScoringPlayer.getPlayerId()) {
                        // Add bonus to every other player
                        double updatedContribution = otherPlayer.getContribution() + PENALTY_HIGH_SCORE;
                        otherPlayer.setContribution(updatedContribution);
                    }
                }
            }
        }

        // Return the updated list of ResultDTO
        return results;
    }


}
