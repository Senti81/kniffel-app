package de.coin.kniffel.model.dto;

import de.coin.kniffel.util.DateTimeUtils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class GameScoreViewDTO {

    private final IntegerProperty year = new SimpleIntegerProperty();
    private final IntegerProperty nr = new SimpleIntegerProperty();
    private final StringProperty date = new SimpleStringProperty();
    private final IntegerProperty gameId = new SimpleIntegerProperty();
    private Map<String, Integer> playerScores;

    public GameScoreViewDTO() {}

    public GameScoreViewDTO(int gameId, int year, int nr, LocalDate date, Map<String, Integer> playerScores) {
        this.gameId.set(gameId);
        this.year.set(year);
        this.nr.set(nr);
        this.date.set(date != null ? DateTimeUtils.formatDate(date) : "");
        this.playerScores = playerScores;
    }

    public int getGameId() {
        return gameId.get();
    }

    public IntegerProperty gameIdProperty() {
        return gameId;
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public int getNr() {
        return nr.get();
    }

    public IntegerProperty nrProperty() {
        return nr;
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public Integer getScoreForPlayer(String playerName) {
        return playerScores != null ? playerScores.get(playerName) : null;
    }

    public void setDateFromString(String dateStr) {
        if (dateStr != null && !dateStr.isBlank() && DateTimeUtils.isValidDate(dateStr)) {
            this.date.set(DateTimeUtils.formatDate(DateTimeUtils.parseDate(dateStr)));
        }
    }
}
