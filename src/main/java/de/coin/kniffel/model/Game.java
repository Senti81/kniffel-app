package de.coin.kniffel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Game {

    private final IntegerProperty gameId = new SimpleIntegerProperty();

    private int nr;
    private int year;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Game() {}

    public Game(int year, int nr, LocalDate date) {
        this.year = year;
        this.nr = nr;
        this.date = date;
    }

    public int getGameId() {
        return gameId.get();
    }

    public void setGameId(int gameId) {
        this.gameId.set(gameId);
    }

}
