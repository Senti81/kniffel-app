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
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

    // Current Season Table
    public TableView<GameResultDTO> tableSeasonResults;
    public TableColumn<GameResultDTO, Integer> columnSeasonPosition;
    public TableColumn<GameResultDTO, String> columnSeasonName;
    public TableColumn<GameResultDTO, Integer> columnSeasonScore;
    public TableColumn<GameResultDTO, Double> columnSeasonContribution;

    public List<GameResultDTO> listGameResults;
    public List<GameResultDTO> listGameResults2;

    public List<GameResultDTO> listSeasonResults;
    public List<GameResultDTO> listSeasonResults2;

    private final GameService gameService = new GameService();
    private final ScoreService scoreService = new ScoreService();

    public Button buttonPrint;
    public Label labelGame;
    public Label labelSeason;

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
            log.debug("Selected year: {}", selectedYear);
        });

        comboBoxGameNumber.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                selectedGameNumber = newValue;
                selectedGameDate = gameService.getGameDateByYearAndNumber(selectedYear, selectedGameNumber);

                listGameResults = scoreService.getGameResultsByYearAndGameNumber(selectedYear, selectedGameNumber);
                listSeasonResults = scoreService.getSeasonResultsByYear(selectedYear, selectedGameNumber);

                // set table views
                tableGameResults.setItems(FXCollections.observableArrayList(listGameResults));
                tableSeasonResults.setItems(FXCollections.observableArrayList(listSeasonResults));

                labelGame.setText(String.format("Ergebnis von Spiel %d ", selectedGameNumber));
                labelSeason.setText(String.format("Gesamtergebnis nach allen Spielen aus %d", selectedYear));

                // TODO Diese beiden DTOs sind nur für einen Export relevant. Werden nicht in der Tabelle angezeigt, aber für den aktuellen Export noch benötigt
                // TODO Evtl. auslagern in seperate Funktionalität (z.B. Dropdown für PDF Export machen)
                listGameResults2 = scoreService.getGameResultsByYearAndGameNumber(selectedYear, selectedGameNumber + 1);
                listSeasonResults2 = scoreService.getSeasonResultsByYear(selectedYear, selectedGameNumber + 1);
            }
        });

        setupTableColumns();
    }

    private void setupTableColumns() {
        columnPosition.setCellValueFactory(data -> data.getValue().positionProperty().asObject());
        columnName.setCellValueFactory(data -> data.getValue().playerNameProperty());
        columnScore.setCellValueFactory(data -> data.getValue().finalScoreProperty().asObject());
        columnContribution.setCellValueFactory(data -> data.getValue().contributionProperty().asObject());
        columnSeasonPosition.setCellValueFactory(data -> data.getValue().positionProperty().asObject());
        columnSeasonName.setCellValueFactory(data -> data.getValue().playerNameProperty());
        columnSeasonScore.setCellValueFactory(data -> data.getValue().finalScoreProperty().asObject());
        columnSeasonContribution.setCellValueFactory(data -> data.getValue().contributionProperty().asObject());
    }

    public void handlePrint(ActionEvent actionEvent) {
        log.info("Printing the following information to PDF: {} {} {} {}", listGameResults, listGameResults2, listSeasonResults, listSeasonResults2);
        PdfUtils.createPdf(listGameResults, listGameResults2, listSeasonResults, listSeasonResults2, selectedYear, selectedGameNumber, selectedGameDate);
    }
}
