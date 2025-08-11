package de.coin.kniffel.model.dto;

import java.time.LocalDate;

public class GameDTO {

    private int gameNumber;
    private int gameYear;
    private LocalDate gameDate;

    public GameDTO(int gameNumber, int gameYear, LocalDate gameDate) {
        this.gameNumber = gameNumber;
        this.gameYear = gameYear;
        this.gameDate = gameDate;
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

    @Override
    public String toString() {
        return "GameDTO{" +
                "gameNumber=" + gameNumber +
                ", gameYear=" + gameYear +
                ", gameDate=" + gameDate +
                '}';
    }
}
