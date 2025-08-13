package de.coin.kniffel.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import de.coin.kniffel.model.dto.GameResultDTO;
import de.coin.kniffel.service.GameService;
import de.coin.kniffel.service.ScoreService;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ScoreTableViewController implements Initializable {

    public TableView<GameResultDTO> scoreTable;
    public TableColumn<GameResultDTO, Integer> positionColumn;
    public TableColumn<GameResultDTO, String> playerNameColumn;
    public TableColumn<GameResultDTO, Integer> finalScoreColumn;
    public Button backButton;

    private final GameService gameService = new GameService();
    private final ScoreService service = new ScoreService();
    public Label gameInfos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        finalScoreColumn.setCellValueFactory(new PropertyValueFactory<>("finalScore"));
        gameInfos.setText("Aktueller Stand nach Spiel: " + gameService.getLatestGameNumber());
        service.getTotalScoreFromYear(LocalDate.now().getYear()).forEach(scoreDTO -> scoreTable.getItems().add(scoreDTO));
        backButton.setOnAction(e -> ((Stage) backButton.getScene().getWindow()).close());

    }

}
