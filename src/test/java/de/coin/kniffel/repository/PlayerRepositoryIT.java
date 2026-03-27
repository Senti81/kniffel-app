package de.coin.kniffel.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.coin.kniffel.model.Player;
import de.coin.kniffel.util.DatabaseUtil;

public class PlayerRepositoryIT {

    private PlayerRepository repository;

    @BeforeEach
    void setUp() {
        DatabaseUtil.cleanDatabase();
        repository = new PlayerRepository();
    }

    @Test
    void save_ShouldPersistPlayerAndRetrieveById() {
        Player player = new Player("Alice");
        repository.save(player);

        List<Player> allPlayers = repository.findAll();
        assertEquals(1, allPlayers.size());
        assertEquals("Alice", allPlayers.getFirst().getPlayerName());
        allPlayers.getFirst().getPlayerId();
    }

    @Test
    void save_MultiplePlayers_ShouldRetrieveAll() {
        repository.save(new Player("Alice"));
        repository.save(new Player("Bob"));
        repository.save(new Player("Charlie"));

        List<Player> allPlayers = repository.findAll();
        assertEquals(3, allPlayers.size());
    }

    @Test
    void update_ShouldChangePlayerName() {
        Player player = new Player("Alice");
        repository.save(player);

        List<Player> players = repository.findAll();
        Player savedPlayer = players.getFirst();
        int playerId = savedPlayer.getPlayerId();

        savedPlayer.setPlayerName("Alicia");
        repository.update(savedPlayer);

        List<Player> updatedPlayers = repository.findAll();
        assertEquals(1, updatedPlayers.size());
        assertEquals("Alicia", updatedPlayers.getFirst().getPlayerName());
        assertEquals(playerId, updatedPlayers.getFirst().getPlayerId());
    }

    @Test
    void delete_ShouldRemovePlayerFromDatabase() {
        repository.save(new Player("Alice"));
        repository.save(new Player("Bob"));

        List<Player> beforeDelete = repository.findAll();
        assertEquals(2, beforeDelete.size());

        Player playerToDelete = beforeDelete.getFirst();
        repository.delete(playerToDelete);

        List<Player> afterDelete = repository.findAll();
        assertEquals(1, afterDelete.size());
        assertEquals("Bob", afterDelete.getFirst().getPlayerName());
    }

    @Test
    void findAll_EmptyDatabase_ShouldReturnEmptyList() {
        List<Player> players = repository.findAll();
        assertTrue(players.isEmpty());
    }

    @AfterEach
    void tearDown() {
        DatabaseUtil.cleanDatabase();
    }
}
