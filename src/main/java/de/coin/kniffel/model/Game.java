package de.coin.kniffel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

public class Game {

    private final IntegerProperty gameId = new SimpleIntegerProperty();
//    private int id;

    @Getter
    @Setter
    private int nr;

    @Getter
    @Setter
    private int year;

    @Getter
    @Setter
    private LocalDate date;

    @Getter
    @Setter
    private LocalDateTime createdAt;

    @Getter
    @Setter
    private LocalDateTime updatedAt;

    public Game() {}

    public Game(int year, LocalDate date) {
        this.year = year;
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getGameId() {
        return gameId.get();
    }

    public void setGameId(int gameId) {
        this.gameId.set(gameId);
    }

    public IntegerProperty gameIdProperty() {
        return gameId;
    }
}
