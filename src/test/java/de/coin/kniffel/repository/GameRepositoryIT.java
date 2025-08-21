package de.coin.kniffel.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.util.DatabaseUtil;
import de.coin.kniffel.util.TestDataGenerator;

public class GameRepositoryIT {

    GameRepository repository = new GameRepository();

    @BeforeEach
    void setUp() {
        DatabaseUtil.cleanDatabase();
    }

    @Test
    void verifyThatAllPlayersAreSaved() {

        // Arrange
        List<Game> games = TestDataGenerator.generateGamesForYear(2024);

        // Act
        for (Game game : games) repository.save(game);

        // Assert
        assertEquals(52, repository.findAll().size());
    }
}
