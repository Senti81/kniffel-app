package de.coin.kniffel.model.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ScoreDTO {

    private int gameNumber;
    private int gameYear;
    private LocalDate gameDate;
    private String playerName;
    private int finalScore;

}
