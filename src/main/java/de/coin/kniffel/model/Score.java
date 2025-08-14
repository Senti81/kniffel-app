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

//    public Score() {
//    }
//
//    public Score(int gameId, int playerId, int finalScore) {
//        this.gameId = gameId;
//        this.playerId = playerId;
//        this.finalScore = finalScore;
//    }
//
//    public int getGameId() {
//        return gameId;
//    }
//
//    public void setGameId(int gameId) {
//        this.gameId = gameId;
//    }
//
//    public int getPlayerId() {
//        return playerId;
//    }
//
//    public void setPlayerId(int playerId) {
//        this.playerId = playerId;
//    }
//
//    public int getFinalScore() {
//        return finalScore;
//    }
//
//    public void setFinalScore(int finalScore) {
//        this.finalScore = finalScore;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
}
