package de.coin.kniffel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Game {

    private int id;
    private int nr;
    private int year;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
