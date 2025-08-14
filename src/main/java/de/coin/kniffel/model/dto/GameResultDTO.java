package de.coin.kniffel.model.dto;

import lombok.Data;

@Data
public class GameResultDTO {

    private Integer position;
    private String playerName;
    private Integer finalScore;
    private Double contribution;

}
