package de.coin.kniffel.model.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ScoreDTO {

    private final int gameNumber;
    private final int gameYear;
    private final LocalDate gameDate;
    private final String playerName;
    private final int finalScore;

}
