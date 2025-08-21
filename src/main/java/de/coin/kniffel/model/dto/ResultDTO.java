package de.coin.kniffel.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResultDTO {

    private int gameId;
    private int playerId;
    private int score;
    private double contribution;

    public ResultDTO(int gameId, int playerId, int score) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.score = score;
    }
}
