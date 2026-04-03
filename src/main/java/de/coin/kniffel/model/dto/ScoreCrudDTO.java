package de.coin.kniffel.model.dto;

import javafx.beans.property.*;
import lombok.Data;

@Data
public class ScoreCrudDTO {

    private IntegerProperty gameId = new SimpleIntegerProperty();
    private IntegerProperty playerId = new SimpleIntegerProperty();
    private IntegerProperty score = new SimpleIntegerProperty();
    private DoubleProperty contribution = new SimpleDoubleProperty();
    private StringProperty playerName = new SimpleStringProperty();
    private StringProperty gameInfo = new SimpleStringProperty();

    public IntegerProperty gameIdProperty() {
        return gameId;
    }

    public IntegerProperty playerIdProperty() {
        return playerId;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public DoubleProperty contributionProperty() {
        return contribution;
    }

    public StringProperty playerNameProperty() {
        return playerName;
    }

    public StringProperty gameInfoProperty() {
        return gameInfo;
    }
}
