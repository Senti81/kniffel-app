package de.coin.kniffel.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.model.Player;
import net.datafaker.Faker;

public class TestDataGenerator {

    private static final Faker faker = new Faker();

    public static List<Player> generatePlayers(int count) {
        return IntStream.range(0, count).mapToObj(i -> generatePlayer()).toList();
    }

    public static List<Game> generateGamesForYear(int year) {
        return IntStream.range(0, 52).mapToObj(i -> createGame(i, year)).toList();
    }

    public static Player generatePlayer() {
        Player player = new Player();
        player.setPlayerName(faker.name().firstName());
        return player;
    }

    public static Game createGame(int index, int year) {
        Game game = new Game();
        game.setNr(index + 1);
        game.setYear(year);
        LocalDate date = LocalDate.of(year, 1, 1).with(DayOfWeek.FRIDAY).plusWeeks(index);
        game.setDate(date);
        return game;
    }

}
