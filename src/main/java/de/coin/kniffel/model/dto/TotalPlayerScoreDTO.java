package de.coin.kniffel.model.dto;

import lombok.Data;

@Data
public class TotalPlayerScoreDTO {

    private final Integer position;
    private final String playerName;
    private final Integer totalScore;

}
