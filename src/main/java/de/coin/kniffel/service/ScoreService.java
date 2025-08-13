package de.coin.kniffel.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.coin.kniffel.model.Score;
import de.coin.kniffel.model.dto.GameResultDTO;
import de.coin.kniffel.repository.ScoreRepository;

public class ScoreService {

    private static final Logger log = LoggerFactory.getLogger(ScoreService.class);

    private static final double DEFAULT_CONTRIBUTION = 1.50D;
    private static final double PENALTY_LOW_SCORE = 2.00D;
    private static final int MIN_SCORE_THRESHOLD = 20;
    private static final int MAX_SCORE_THRESHOLD = 500;

    private final ScoreRepository scoreRepository;

    public ScoreService() {
        scoreRepository = new ScoreRepository();
    }

    public void saveScore(Score score) {
        scoreRepository.save(score);
    }

    public List<GameResultDTO> getTotalScoreFromYear(int year) {
        return scoreRepository.getTotalScoreOfEachPlayerByYear(year);
    }

    public List<GameResultDTO> getScoreFromYearAndGame(int year, int gameNumber) {
        List<GameResultDTO> gameResultList = scoreRepository.getScoreOfEachPlayerByYearAndGame(year, gameNumber);
        return calculateContributions(gameResultList);
    }

    private List<GameResultDTO> calculateContributions(List<GameResultDTO> gameResultList) {
        for (GameResultDTO gameResult : gameResultList) {
            double contribution = gameResult.getPosition() * DEFAULT_CONTRIBUTION;
            log.info("{} is at position {}. Contribution is {}", gameResult.getPlayerName(), gameResult.getPosition(), contribution);
            gameResult.setContribution(contribution);
            if (gameResult.getFinalScore() < MIN_SCORE_THRESHOLD) {
                log.info("{} has a score of {}. Is below the minimum threshold of {}. Adding 2.00", gameResult.getPlayerName(), gameResult.getFinalScore(), MIN_SCORE_THRESHOLD);
                gameResult.setContribution(gameResult.getContribution() + PENALTY_LOW_SCORE);
                log.info("Contribution is now {}", gameResult.getContribution());
            }
        }
        return gameResultList;
    }

}
