package de.coin.kniffel.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.model.Player;
import de.coin.kniffel.model.Score;
import de.coin.kniffel.model.dto.GameResultDTO;
import de.coin.kniffel.model.dto.ResultDTO;
import de.coin.kniffel.service.ScoreService;
import de.coin.kniffel.util.DatabaseUtil;
import de.coin.kniffel.util.PdfUtils;

public class ScoreRepositoryIT {

    private PlayerRepository playerRepository;
    private GameRepository gameRepository;
    private ScoreRepository scoreRepository;
    private ScoreService scoreService;

    private static final String PDF_OUTPUT_PATH = "data/2026_1_2.pdf";

    @BeforeEach
    void setUp() {
        DatabaseUtil.cleanDatabase();
        playerRepository = new PlayerRepository();
        gameRepository = new GameRepository();
        scoreRepository = new ScoreRepository();
        scoreService = new ScoreService();
    }

    @AfterEach
    void tearDown() {
        DatabaseUtil.cleanDatabase();
        new File(PDF_OUTPUT_PATH).delete();
    }

    @Test
    void endToEnd_CreatePlayersGamesScoresAndExportPdf() {
        // Create 4 players
        playerRepository.save(new Player("Alice"));
        playerRepository.save(new Player("Bob"));
        playerRepository.save(new Player("Charles"));
        playerRepository.save(new Player("Danny"));
        List<Player> players = playerRepository.findAll();
        assertEquals(4, players.size());

        // Create Game 1 (2026-01-02)
        Game game1 = new Game(2026, 1, LocalDate.of(2026, 1, 2));
        int gameId1 = gameRepository.save(game1);

        // Create Game 2 (2026-01-09)
        Game game2 = new Game(2026, 2, LocalDate.of(2026, 1, 9));
        int gameId2 = gameRepository.save(game2);

        // Create scores for Game 1 (scores between 899 and 1300)
        scoreRepository.save(createScore(gameId1, players.get(0).getPlayerId(), 1200));
        scoreRepository.save(createScore(gameId1, players.get(1).getPlayerId(), 1100));
        scoreRepository.save(createScore(gameId1, players.get(2).getPlayerId(), 1000));
        scoreRepository.save(createScore(gameId1, players.get(3).getPlayerId(), 899));

        // Create scores for Game 2
        scoreRepository.save(createScore(gameId2, players.get(0).getPlayerId(), 1300));
        scoreRepository.save(createScore(gameId2, players.get(1).getPlayerId(), 1150));
        scoreRepository.save(createScore(gameId2, players.get(2).getPlayerId(), 950));
        scoreRepository.save(createScore(gameId2, players.get(3).getPlayerId(), 1050));

        // Calculate contributions for Game 1
        List<ResultDTO> game1Results = scoreRepository.getResultsByGameId(gameId1);
        List<ResultDTO> updatedGame1Results = scoreService.calculateContributions(game1Results);
        for (ResultDTO result : updatedGame1Results) {
            scoreRepository.updateContribution(gameId1, result.getPlayerId(), result.getContribution());
        }

        // Calculate contributions for Game 2
        List<ResultDTO> game2Results = scoreRepository.getResultsByGameId(gameId2);
        List<ResultDTO> updatedGame2Results = scoreService.calculateContributions(game2Results);
        for (ResultDTO result : updatedGame2Results) {
            scoreRepository.updateContribution(gameId2, result.getPlayerId(), result.getContribution());
        }

        // Get data for PDF export
        List<GameResultDTO> game1SeasonResults = scoreRepository.getSeasonResultsByYear(2026, 1);
        List<GameResultDTO> game1GameResults = scoreRepository.getScoreOfEachPlayerByYearAndGame(2026, 1);
        List<GameResultDTO> game2SeasonResults = scoreRepository.getSeasonResultsByYear(2026, 2);
        List<GameResultDTO> game2GameResults = scoreRepository.getScoreOfEachPlayerByYearAndGame(2026, 2);

        // Export to PDF
        PdfUtils.createPdf(
                game1GameResults,
                game2GameResults,
                game1SeasonResults,
                game2SeasonResults,
                2026,
                List.of(1, 2),
                LocalDate.of(2026, 1, 2),
                false
        );

        // Verify PDF was created
        File pdfFile = new File(PDF_OUTPUT_PATH);
        assertTrue(pdfFile.exists(), "PDF file should exist at: " + PDF_OUTPUT_PATH);
        assertTrue(pdfFile.length() > 0, "PDF file should not be empty");
    }

    private Score createScore(int gameId, int playerId, int finalScore) {
        Score score = new Score();
        score.setGameId(gameId);
        score.setPlayerId(playerId);
        score.setFinalScore(finalScore);
        return score;
    }
}
