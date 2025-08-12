package de.coin.kniffel.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.coin.kniffel.model.Score;
import de.coin.kniffel.model.dto.ScoreDTO;
import de.coin.kniffel.model.dto.TotalPlayerScoreDTO;
import de.coin.kniffel.repository.ScoreRepository;

public class ScoreService {

    private static final Logger log = LoggerFactory.getLogger(ScoreService.class);
    private final ScoreRepository scoreRepository;

    public ScoreService() {
        scoreRepository = new ScoreRepository();
    }

    public void saveScore(Score score) {
        scoreRepository.save(score);
    }

    public List<TotalPlayerScoreDTO> getTotalScoreFromYear(int year) {
        return scoreRepository.getTotalScoreOfEachPlayerByYear(year);
    }

    public List<ScoreDTO> getAllScores() {
        return scoreRepository.findAll();
    }
}
