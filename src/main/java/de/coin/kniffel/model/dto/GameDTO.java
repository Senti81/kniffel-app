package de.coin.kniffel.model.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class GameDTO {

    private final int gameNumber;
    private final int gameYear;
    private final LocalDate gameDate;

}
