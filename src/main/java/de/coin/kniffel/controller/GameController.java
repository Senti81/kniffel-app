package de.coin.kniffel.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.model.Player;
import de.coin.kniffel.model.Score;
import de.coin.kniffel.model.dto.GameDTO;
import de.coin.kniffel.service.GameService;
import de.coin.kniffel.service.PlayerService;
import de.coin.kniffel.service.ScoreService;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameController implements Initializable {

    private final PlayerService playerService = new PlayerService();
    private final GameService gameService = new GameService();
    private final ScoreService scoreService = new ScoreService();

    public TextField gameNumberField;
    public TextField gameYearField;
    public DatePicker datePicker;
    public GridPane playerGrid;
    public Button backButton;

    private final Map<Player, TextField> playerScoresMap = new HashMap<>();

    LocalDate minGameDate = LocalDate.of(1990, 1, 1 );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Player> players = fetchUsersFromDatabase();

        GameDTO latestGame = gameService.getLatestGame();

        if (latestGame == null) {
            log.info("No games found in database. Initializing with default values.");
            latestGame = new GameDTO();
            latestGame.setGameNumber(0);
            latestGame.setGameYear(LocalDate.now().getYear());
        } else {
            minGameDate = latestGame.getGameDate().plusDays(1);
        }

        gameNumberField.setText(String.valueOf(latestGame.getGameNumber() + 1));
        gameYearField.setText(String.valueOf(latestGame.getGameYear()));
        datePicker.setPromptText("mind. " + minGameDate);

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Label label = new Label(player.getPlayerName());
            GridPane.setRowIndex(label, i + 3);
            GridPane.setColumnIndex(label, 0);

            TextField scoreField = new TextField();
            scoreField.setPromptText("Punkte");
            GridPane.setRowIndex(scoreField, i + 3);
            GridPane.setColumnIndex(scoreField, 1);

            playerGrid.getChildren().addAll(label, scoreField);
            System.out.println(scoreField.getText());
            playerScoresMap.put(player, scoreField);
        }

        backButton.setOnAction(e -> ((javafx.stage.Stage) backButton.getScene().getWindow()).close());
    }

    public void handleSubmit() {
        int gameNumber = Integer.parseInt(gameNumberField.getText());
        int gameYear = Integer.parseInt(gameYearField.getText());
        LocalDate gameDate = LocalDate.parse(
                datePicker.getValue() != null ? datePicker.getValue().toString() : String.valueOf(minGameDate)
        );

        if (gameDate.isBefore(minGameDate)) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Das Datum muss nach dem letzten Spiel liegen. (" + minGameDate + ")");
            return;
        }

        Game gameToBeSaved = new Game();
        gameToBeSaved.setYear(gameYear);
        gameToBeSaved.setNr(gameNumber);
        gameToBeSaved.setDate(gameDate);

        int newGameId = gameService.saveGame(gameToBeSaved);
        if (newGameId == -1) {
            log.warn("Game number {} of year {} already exists. Skipping", gameNumber, gameYear);
            showAlert(Alert.AlertType.ERROR, "Fehler", "Spielnummer existiert bereits.");
            return;
        }

        for (Map.Entry<Player, TextField> entry : playerScoresMap.entrySet()) {
            int playerId = entry.getKey().getPlayerId();
            String playerName = entry.getKey().getPlayerName();
            if (entry.getValue().getText().isEmpty() || entry.getValue().getText().equals("0")) {
                log.info("Skipping score for player {}({}): {}", playerName, playerId, entry.getValue().getText());
            } else {
                int playerScore = Integer.parseInt(entry.getValue().getText());
                Score score = new Score();
                score.setPlayerId(playerId);
                score.setGameId(newGameId);
                score.setFinalScore(playerScore);
                log.info("Saving score for player {}({}): {}", playerName, playerId, playerScore);
                scoreService.saveScore(score);
            }
        }


        log.info("Game with number {} saved successfully", gameNumber);
        showAlert(Alert.AlertType.INFORMATION, "Information", "Spiel erfolgreich gespeichert.");

        // Calculate contribution for each player
        scoreService.getResultsByGameId(newGameId);

        Stage currentStage = (Stage) gameNumberField.getScene().getWindow();
        currentStage.close();

    }

    /**
     * Retrieves a list of all players from the database.
     *
     * @return a list of Player objects representing all users stored in the database
     */
    private List<Player> fetchUsersFromDatabase() {
        return playerService.getAllPlayers();
    }

    /**
     * Utility function to show alerts.
     */
    public void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
