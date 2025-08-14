package de.coin.kniffel.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.model.dto.GameDTO;
import de.coin.kniffel.repository.GameRepository;

public class GameService {

    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameRepository gameRepository;

    public GameService() {
        gameRepository = new GameRepository();
    }

    public int saveGame(Game game) {
        Game foundByNumberAndYear = gameRepository.findByGameNumberAndYear(game.getNr(), game.getYear());
        return foundByNumberAndYear != null ? -1 : gameRepository.save(game);
    }

    public List<GameDTO> getAllGames() {
        return gameRepository.findAll();
    }

    public int getLatestGameNumber() {
        return gameRepository.findLatestGameNumber();
    }

    public int determineNewGameNumber() {
        log.info("Calculating most recent game number");
        int newGameNumber = gameRepository.findLatestGameNumber() + 1;
        log.info("Calculated new game number: {}", newGameNumber);
        return newGameNumber;
    }
}
