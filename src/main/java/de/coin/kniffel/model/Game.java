package de.coin.kniffel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Data;

@Data
public class Game {

    private final IntegerProperty gameId = new SimpleIntegerProperty();
    private int nr;
    private int year;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
