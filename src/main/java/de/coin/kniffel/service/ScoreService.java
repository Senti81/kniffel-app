package de.coin.kniffel.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.coin.kniffel.model.Score;
import de.coin.kniffel.model.dto.GameResultDTO;
import de.coin.kniffel.model.dto.ResultDTO;
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

    public List<GameResultDTO> getSeasonResultsByYear(int year) {
        return scoreRepository.getSeasonResultsByYear(year);
    }

    public List<GameResultDTO> getGameResultsByYearAndGameNumber(int year, int gameNumber) {
        return scoreRepository.getScoreOfEachPlayerByYearAndGame(year, gameNumber);
    }

    public void getResultsByGameId(int gameId) {
        List<ResultDTO> results = scoreRepository.getResultsByGameId(gameId);
        for (ResultDTO result : results) {
            updateContribution(result.getGameId(), result.getPlayerId(), (results.indexOf(result) + 1) * DEFAULT_CONTRIBUTION);
        }
    }

    private void updateContribution(int gameId, int playerId, double contribution) {
        scoreRepository.updateContribution(gameId, playerId, contribution);
    }

    private List<GameResultDTO> calculateContributions(List<GameResultDTO> gameResultList) {
        for (GameResultDTO gameResult : gameResultList) {
            double contribution = gameResult.getPosition().get() * DEFAULT_CONTRIBUTION;
            log.info("{} is at position {}. Contribution is {}", gameResult.getPlayerName().get(), gameResult.getPosition().get(), contribution);
            gameResult.getContribution().set(contribution);
            if (gameResult.getFinalScore().intValue() < MIN_SCORE_THRESHOLD) {
                log.info("{} has a score of {}. Is below the minimum threshold of {}. Adding 2.00", gameResult.getPlayerName().get(), gameResult.getFinalScore().get(), MIN_SCORE_THRESHOLD);
                gameResult.getContribution().set(gameResult.getContribution().get() + PENALTY_LOW_SCORE);
                log.info("Contribution is now {}", gameResult.getContribution());
            }
        }
        return gameResultList;
    }

}
