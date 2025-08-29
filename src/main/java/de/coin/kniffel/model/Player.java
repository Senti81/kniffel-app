package de.coin.kniffel.model;

import java.time.LocalDateTime;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

public class Player {

    private final IntegerProperty playerId = new SimpleIntegerProperty();
    private final StringProperty playerName = new SimpleStringProperty();
    @Setter
    @Getter
    private LocalDateTime createdAt;
    @Setter
    @Getter
    private LocalDateTime updatedAt;

    public Player() {}

    public Player(String name) {
        this.playerName.set(name);
    }

    public int getPlayerId() {
        return playerId.get();
    }

    public void setPlayerId(int playerId) {
        this.playerId.set(playerId);
    }

    public String getPlayerName() {
        return playerName.get();
    }

    public void setPlayerName(String playerName) {
        this.playerName.set(playerName);
    }

}
