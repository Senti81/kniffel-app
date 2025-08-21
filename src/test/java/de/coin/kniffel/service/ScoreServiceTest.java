package de.coin.kniffel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.coin.kniffel.model.dto.ResultDTO;

public class ScoreServiceTest {

    private ScoreService scoreService;

    @BeforeEach
    public void setUp() {
        scoreService = new ScoreService();
    }

    @Test
    void calculateContributions_WithHighAndLowScore_ShouldReturnCorrectContribution() {

        // Arrange
        List<ResultDTO> results = new ArrayList<>();

        results.add(new ResultDTO(1, 1, 1001));
        results.add(new ResultDTO(1, 2, 500));
        results.add(new ResultDTO(1, 3, 300));
        results.add(new ResultDTO(1, 4, 199));

        // Act
        List<ResultDTO> updatedResults = scoreService.calculateContributions(results);
        System.out.println(updatedResults);

        // Asert
        assertEquals(1.50D, updatedResults.get(0).getContribution());
        assertEquals(3.50D, updatedResults.get(1).getContribution());
        assertEquals(5.00D, updatedResults.get(2).getContribution());
        assertEquals(8.50D, updatedResults.get(3).getContribution());
    }

    @Test
    void calculateContributions_WithOnlyHighScores_ShouldAddBonuses() {
        // Arrange
        List<ResultDTO> results = new ArrayList<>();
        results.add(new ResultDTO(1, 1, 1100));
        results.add(new ResultDTO(1, 2, 1200));
        results.add(new ResultDTO(1, 3, 1300));
        results.add(new ResultDTO(1, 4, 1400));

        // Act
        List<ResultDTO> updatedResults = scoreService.calculateContributions(results);

        // Assert
        assertEquals(3.00D, updatedResults.get(0).getContribution());
        assertEquals(4.50D, updatedResults.get(1).getContribution());
        assertEquals(6.00D, updatedResults.get(2).getContribution());
        assertEquals(7.50D, updatedResults.get(3).getContribution());
    }

    @Test
    void calculateContributions_WithOnlyLowScores_ShouldAddPenalties() {
        // Arrange
        List<ResultDTO> results = new ArrayList<>();
        results.add(new ResultDTO(1, 1, 25));  // Below MIN_SCORE_THRESHOLD
        results.add(new ResultDTO(1, 2, 50));  // Below MIN_SCORE_THRESHOLD
        results.add(new ResultDTO(1, 3, 75)); // Below MIN_SCORE_THRESHOLD
        results.add(new ResultDTO(1, 4, 100)); // Below MIN_SCORE_THRESHOLD

        // Act
        List<ResultDTO> updatedResults = scoreService.calculateContributions(results);

        // Assert
        assertEquals(3.50D, updatedResults.get(0).getContribution());
        assertEquals(5.00D, updatedResults.get(1).getContribution());
        assertEquals(6.50D, updatedResults.get(2).getContribution());
        assertEquals(8.00D, updatedResults.get(3).getContribution());
    }
}
