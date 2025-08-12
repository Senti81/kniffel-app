package de.coin.kniffel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.Setter;

public class Game {

    private final IntegerProperty gameId = new SimpleIntegerProperty();
    @Setter
    @Getter
    private int nr;
    @Setter
    @Getter
    private int year;
    @Setter
    @Getter
    private LocalDate date;
    @Setter
    @Getter
    private LocalDateTime createdAt;
    @Setter
    @Getter
    private LocalDateTime updatedAt;

    public Game() {}

    public Game(int nr, int year, LocalDate date) {
        this.nr = nr;
        this.year = year;
        this.date = date;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getGameId() {
        return gameId.get();
    }

    public IntegerProperty gameIdProperty() {
        return gameId;
    }

}
