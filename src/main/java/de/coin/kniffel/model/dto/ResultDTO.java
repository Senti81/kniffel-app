package de.coin.kniffel.model.dto;

import lombok.Data;

@Data
public class ResultDTO {

    private int gameId;
    private int playerId;
    private int score;
    private double contribution;
}
