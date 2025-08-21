package de.coin.kniffel.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.coin.kniffel.model.Player;
import de.coin.kniffel.util.DatabaseUtil;
import de.coin.kniffel.util.TestDataGenerator;

public class PlayerRepositoryIT {

    PlayerRepository playerRepository = new PlayerRepository();

    @BeforeEach
    void setUp() {
        DatabaseUtil.cleanDatabase();
    }

    @Test
    void verifyThatAllPlayersAreSaved() {

        // Arrange
        List<Player> players = TestDataGenerator.generatePlayers(4);

        // Act
        for (Player player : players) playerRepository.save(player);

        // Assert
        assertEquals(4, playerRepository.findAll().size());
    }
}
