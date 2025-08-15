package de.coin.kniffel.model.dto;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;

@Data
public class GameResultDTO {

    private IntegerProperty position = new SimpleIntegerProperty();
    private StringProperty playerName = new SimpleStringProperty();
    private IntegerProperty finalScore = new SimpleIntegerProperty();
    private DoubleProperty contribution = new SimpleDoubleProperty();

    public IntegerProperty positionProperty() {
        return position;
    }

    public StringProperty playerNameProperty() {
        return playerName;
    }

    public IntegerProperty finalScoreProperty() {
        return finalScore;
    }

    public DoubleProperty contributionProperty() {
        return contribution;
    }
}
