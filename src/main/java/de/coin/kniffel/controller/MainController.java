package de.coin.kniffel.controller;

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private void handlePlayerManagement() {
        loadView("player-crud-view.fxml", "Kniffel - Spieler");
    }

    @FXML
    private void handleScoreManagement() {
        loadView("add-game-view.fxml", "Kniffel - Spiel");
    }

    @FXML
    public void handleGameView() {
        loadView("game-crud-view.fxml", "Kniffel - Spiel");
    }

    @FXML
    private void handleScoreView()  {
        loadView("game-result-view.fxml", "Kniffel - Score");
    }

    /**
     * Helper method to switch the scene using the given FXML file path.
     *
     * @param fxmlPath the path to the FXML file for the scene
     */
    private void loadView(String fxmlPath, String title) {
        final String prefix = "/de/coin/kniffel/";
        try {
            Node view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(prefix + fxmlPath)));
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Could not load the FXML file: " + fxmlPath);
        }
    }

    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

}
