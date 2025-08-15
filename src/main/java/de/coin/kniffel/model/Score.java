package de.coin.kniffel.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Score {
    private int gameId;
    private int playerId;
    private int finalScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
