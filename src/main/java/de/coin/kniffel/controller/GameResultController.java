package de.coin.kniffel.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.coin.kniffel.model.dto.GameResultDTO;
import de.coin.kniffel.service.GameService;
import de.coin.kniffel.service.ScoreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class GameResultController implements Initializable {
    public ComboBox<Integer> comboBoxYear;
    public ComboBox<Integer> comboBoxGameNumber;

    public TableView<GameResultDTO> tableGameResults;
    public TableColumn<GameResultDTO, Integer> columnPosition;
    public TableColumn<GameResultDTO, String> columnName;
    public TableColumn<GameResultDTO, Integer> columnScore;
    public TableColumn<GameResultDTO, Double> columnContribution;

    private final GameService gameService = new GameService();
    private final ScoreService scoreService = new ScoreService();
    public Button buttonBack;

    private int selectedYear = 0;
    private int selectedGameNumber = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Integer> allGameYears = gameService.getAllGameYears();
        comboBoxYear.getItems().addAll(allGameYears);

        comboBoxYear.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedYear = newValue;
                List<Integer> allGameNumbersByYear = gameService.getAllGameNumbersByYear(newValue);
                comboBoxGameNumber.getItems().clear();
                comboBoxGameNumber.getItems().addAll(allGameNumbersByYear);
            }
        });

        comboBoxGameNumber.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                selectedGameNumber = newValue;
                List<GameResultDTO> gameResultsByYearAndGameNumber = scoreService.getGameResultsByYearAndGameNumber(selectedYear, selectedGameNumber);
                // Populate Table with GameResultDTO
                ObservableList<GameResultDTO> gameResultDTOS = FXCollections.observableArrayList(gameResultsByYearAndGameNumber);
                tableGameResults.setItems(gameResultDTOS);
            }
        });

        setupTableColumns();
    }

    private void setupTableColumns() {
        columnPosition.setCellValueFactory(data -> data.getValue().positionProperty().asObject());
        columnName.setCellValueFactory(data -> data.getValue().playerNameProperty());
        columnScore.setCellValueFactory(data -> data.getValue().finalScoreProperty().asObject());
        columnContribution.setCellValueFactory(data -> data.getValue().contributionProperty().asObject());
    }

    public void handleClick(ActionEvent actionEvent) {
        ((Stage) buttonBack.getScene().getWindow()).close();
    }
}
