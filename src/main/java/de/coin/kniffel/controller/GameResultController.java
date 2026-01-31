package de.coin.kniffel.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import de.coin.kniffel.model.dto.GameResultDTO;
import de.coin.kniffel.service.GameService;
import de.coin.kniffel.service.ScoreService;
import de.coin.kniffel.util.PdfUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameResultController implements Initializable {
    public ComboBox<Integer> comboBoxYear;
    public ComboBox<Integer> comboBoxGameNumber;

    // Current Game Table
    public TableView<GameResultDTO> tableGameResults;
    public TableColumn<GameResultDTO, Integer> columnPosition;
    public TableColumn<GameResultDTO, String> columnName;
    public TableColumn<GameResultDTO, Integer> columnScore;
    public TableColumn<GameResultDTO, Double> columnContribution;

    // Following Game Table
    public TableView<GameResultDTO> tableGameResults2;
    public TableColumn<GameResultDTO, Integer> columnPosition2;
    public TableColumn<GameResultDTO, String> columnName2;
    public TableColumn<GameResultDTO, Integer> columnScore2;
    public TableColumn<GameResultDTO, Double> columnContribution2;

    // Current Season Table
    public TableView<GameResultDTO> tableSeasonResults;
    public TableColumn<GameResultDTO, Integer> columnSeasonPosition;
    public TableColumn<GameResultDTO, String> columnSeasonName;
    public TableColumn<GameResultDTO, Integer> columnSeasonScore;
    public TableColumn<GameResultDTO, Double> columnSeasonContribution;

    private final GameService gameService = new GameService();
    private final ScoreService scoreService = new ScoreService();

    public Button buttonPrint;

    private int selectedYear = 0;
    private int selectedGameNumber = 0;
    private LocalDate selectedGameDate;

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
            List<GameResultDTO> seasonResultsByYear = scoreService.getSeasonResultsByYear(selectedYear);
            tableSeasonResults.setItems(FXCollections.observableArrayList(seasonResultsByYear));
        });

        comboBoxGameNumber.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                selectedGameNumber = newValue;
                selectedGameDate = gameService.getGameDateByYearAndNumber(selectedYear, selectedGameNumber);
                List<GameResultDTO> gameResultsByYearAndGameNumber = scoreService.getGameResultsByYearAndGameNumber(selectedYear, selectedGameNumber);
                List<GameResultDTO> gameResultsByYearAndGameNumber2 = scoreService.getGameResultsByYearAndGameNumber(selectedYear, selectedGameNumber + 1);
                ObservableList<GameResultDTO> gameResultDTOS = FXCollections.observableArrayList(gameResultsByYearAndGameNumber);
                ObservableList<GameResultDTO> gameResultDTOS2 = FXCollections.observableArrayList(gameResultsByYearAndGameNumber2);
                tableGameResults.setItems(gameResultDTOS);
                tableGameResults2.setItems(gameResultDTOS2);
            }
        });

        setupTableColumns();
    }

    private void setupTableColumns() {
        columnPosition.setCellValueFactory(data -> data.getValue().positionProperty().asObject());
        columnName.setCellValueFactory(data -> data.getValue().playerNameProperty());
        columnScore.setCellValueFactory(data -> data.getValue().finalScoreProperty().asObject());
        columnContribution.setCellValueFactory(data -> data.getValue().contributionProperty().asObject());

        columnPosition2.setCellValueFactory(data -> data.getValue().positionProperty().asObject());
        columnName2.setCellValueFactory(data -> data.getValue().playerNameProperty());
        columnScore2.setCellValueFactory(data -> data.getValue().finalScoreProperty().asObject());
        columnContribution2.setCellValueFactory(data -> data.getValue().contributionProperty().asObject());

        columnSeasonPosition.setCellValueFactory(data -> data.getValue().positionProperty().asObject());
        columnSeasonName.setCellValueFactory(data -> data.getValue().playerNameProperty());
        columnSeasonScore.setCellValueFactory(data -> data.getValue().finalScoreProperty().asObject());
        columnSeasonContribution.setCellValueFactory(data -> data.getValue().contributionProperty().asObject());
    }

    public void handlePrint(ActionEvent actionEvent) {
        PdfUtils.createPdf(tableGameResults,tableGameResults2, tableSeasonResults, selectedYear, selectedGameNumber, selectedGameDate);
    }
}
