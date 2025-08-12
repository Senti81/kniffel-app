package de.coin.kniffel.model.dto;

import java.time.LocalDate;

public class ScoreDTO {

    private int gameNumber;
    private int gameYear;
    private LocalDate gameDate;
    private String playerName;
    private int finalScore;

    public ScoreDTO(int gameNumber, int gameYear, LocalDate gameDate, String playerName, int finalScore) {
        this.gameNumber = gameNumber;
        this.gameYear = gameYear;
        this.gameDate = gameDate;
        this.playerName = playerName;
        this.finalScore = finalScore;
    }

    public int getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(int gameNumber) {
        this.gameNumber = gameNumber;
    }

    public int getGameYear() {
        return gameYear;
    }

    public void setGameYear(int gameYear) {
        this.gameYear = gameYear;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }
}
