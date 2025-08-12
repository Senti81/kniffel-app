package de.coin.kniffel.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Score {
    private int gameId;
    private int playerId;
    private int finalScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Score() {
    }

    public Score(int gameId, int playerId, int finalScore) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.finalScore = finalScore;
    }

}
