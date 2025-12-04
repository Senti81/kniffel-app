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
    void calculateContributions_WithoutHighAndLowScore_ShouldReturnCorrectContribution() {

        // Arrange
        List<ResultDTO> results = new ArrayList<>();

        results.add(new ResultDTO(1, 1, 800));
        results.add(new ResultDTO(1, 2, 1100));
        results.add(new ResultDTO(1, 3, 1100));
        results.add(new ResultDTO(1, 4, 1299));

        // Act
        List<ResultDTO> updatedResults = scoreService.calculateContributions(results);
        System.out.println(updatedResults);

        // Asert
        assertEquals(3.50D, updatedResults.get(0).getContribution());
        assertEquals(4.00D, updatedResults.get(1).getContribution());
        assertEquals(4.50D, updatedResults.get(2).getContribution());
        assertEquals(5.00D, updatedResults.get(3).getContribution());
    }

    @Test
    void calculateContributions_WithHighAndLowScore_ShouldReturnCorrectContribution() {

        // Arrange
        List<ResultDTO> results = new ArrayList<>();

        results.add(new ResultDTO(1, 1, 799));
        results.add(new ResultDTO(1, 2, 1100));
        results.add(new ResultDTO(1, 3, 1200));
        results.add(new ResultDTO(1, 4, 1300));

        // Act
        List<ResultDTO> updatedResults = scoreService.calculateContributions(results);
        System.out.println(updatedResults);

        // Asert
        assertEquals(3.50D + 1.00D + 3.00D, updatedResults.get(0).getContribution());
        assertEquals(4.00D + 1.00D, updatedResults.get(1).getContribution());
        assertEquals(4.50D + 1.00D, updatedResults.get(2).getContribution());
        assertEquals(5.00D, updatedResults.get(3).getContribution());
    }

    @Test
    void calculateContributions_WithOnlyHighScores_ShouldAddBonuses() {
        // Arrange
        List<ResultDTO> results = new ArrayList<>();
        results.add(new ResultDTO(1, 1, 1300));
        results.add(new ResultDTO(1, 2, 1301));
        results.add(new ResultDTO(1, 3, 1400));
        results.add(new ResultDTO(1, 4, 1500));

        // Act
        List<ResultDTO> updatedResults = scoreService.calculateContributions(results);

        // Assert
        // Everyone should add 1.00D for each highscore of the other players
        assertEquals(3.50D + 1.00D + 1.00D + 1.00D, updatedResults.get(0).getContribution());
        assertEquals(4.00D + 1.00D + 1.00D + 1.00D, updatedResults.get(1).getContribution());
        assertEquals(4.50D + 1.00D + 1.00D + 1.00D, updatedResults.get(2).getContribution());
        assertEquals(5.00D + 1.00D + 1.00D + 1.00D, updatedResults.get(3).getContribution());
    }

    @Test
    void calculateContributions_WithOnlyLowScores_ShouldAddPenalties() {
        // Arrange
        List<ResultDTO> results = new ArrayList<>();
        results.add(new ResultDTO(1, 1, 799));
        results.add(new ResultDTO(1, 2, 750));
        results.add(new ResultDTO(1, 3, 700));
        results.add(new ResultDTO(1, 4, 600));

        // Act
        List<ResultDTO> updatedResults = scoreService.calculateContributions(results);

        // Assert
        // Everyone should add 3.00D for low score
        assertEquals(3.50D + 3.00D, updatedResults.get(0).getContribution());
        assertEquals(4.00D + 3.00D, updatedResults.get(1).getContribution());
        assertEquals(4.50D + 3.00D, updatedResults.get(2).getContribution());
        assertEquals(5.00D + 3.00D, updatedResults.get(3).getContribution());
    }
}
