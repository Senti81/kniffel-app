package de.coin.kniffel.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.model.dto.GameDTO;
import de.coin.kniffel.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameService {

    private final GameRepository gameRepository;

    public GameService() {
        gameRepository = new GameRepository();
    }

    public int saveGame(Game game) {
        Game foundByNumberAndYear = gameRepository.findByGameNumberAndYear(game.getNr(), game.getYear());
        return foundByNumberAndYear != null ? -1 : gameRepository.save(game);
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Integer> getAllGameYears() {
        return gameRepository.getAllGameYears();
    }

    public List<Integer> getAllGameNumbersByYear(int year) {
        return gameRepository.getAllGameNumbersByYear(year);
    }

    public LocalDate getGameDateByYearAndNumber(int year, int gameNumber) {
        return gameRepository.getGameDateByYearAndNumber(year, gameNumber);
    }

    public GameDTO getLatestGame() {
        return gameRepository.findLatestGame();
    }

    public void deleteGame(Game game) {
        gameRepository.delete(game);
    }
}
