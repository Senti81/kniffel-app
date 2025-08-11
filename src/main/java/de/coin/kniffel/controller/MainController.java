package de.coin.kniffel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController implements Initializable {

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage = new Stage();
    }

    @FXML
    private void handlePlayerManagement() {
        switchScene("player-view.fxml", "Kniffel - Spieler");
    }

    @FXML
    private void handleScoreManagement() {
//        new ScoreView().start(stage);
        switchScene("add-game-view.fxml", "Kniffel - Spiel");
    }

    @FXML
    public void handleGameView() {
        switchScene("game-view.fxml", "Kniffel - Spiel");
    }

    @FXML
    private void handleScoreView()  {
        switchScene("score-table-view.fxml", "Kniffel - Score");
    }

    /**
     * Helper method to switch the scene using the given FXML file path.
     *
     * @param fxmlPath the path to the FXML file for the scene
     */
    private void switchScene(String fxmlPath, String title) {
        final String prefix = "/de/coin/kniffel/";
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(prefix + fxmlPath)));
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Could not load the FXML file: " + fxmlPath);
        }
    }

    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

}
