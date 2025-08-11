package de.coin.kniffel.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

import de.coin.kniffel.model.dto.ScoreDTO;
import de.coin.kniffel.model.dto.TotalPlayerScoreDTO;
import de.coin.kniffel.repository.ScoreRepository;
import de.coin.kniffel.model.Score;
import de.coin.kniffel.service.GameService;
import de.coin.kniffel.service.ScoreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ScoreTableViewController implements Initializable {

    public TableView<TotalPlayerScoreDTO> scoreTable;
    public TableColumn<TotalPlayerScoreDTO, Integer> positionColumn;
    public TableColumn<TotalPlayerScoreDTO, String> playerNameColumn;
    public TableColumn<TotalPlayerScoreDTO, Integer> totalScoreColumn;
    public Button backButton;

    private final GameService gameService = new GameService();
    private final ScoreService service = new ScoreService();
    public Label gameInfos;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        totalScoreColumn.setCellValueFactory(new PropertyValueFactory<>("totalScore"));
        gameInfos.setText("Aktueller Stand nach Spiel: " + gameService.getLatestGameNumber());
        service.getTotalScoreFromYear(LocalDate.now().getYear()).forEach(scoreDTO -> scoreTable.getItems().add(scoreDTO));
        backButton.setOnAction(e -> ((Stage) backButton.getScene().getWindow()).close());

    }

}
