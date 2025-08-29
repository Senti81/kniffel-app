package de.coin.kniffel.service;

import java.time.LocalDateTime;
import java.util.List;

import de.coin.kniffel.repository.PlayerRepository;
import de.coin.kniffel.model.Player;

/**
 * Service class that provides operations for managing Player entities.
 * This class acts as a layer between the controller and the repository,
 * coordinating the retrieval, creation, updating, and deletion of players.
 */
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService() {
        playerRepository = new PlayerRepository();
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public void addNewPlayer(String name) {
        playerRepository.save(new Player(name));
    }

    public void updatePlayer(Player player, String newName) {
        player.setPlayerName(newName);
        playerRepository.update(player);
    }

    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

}
