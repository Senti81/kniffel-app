package de.coin.kniffel.controller.crud;

import de.coin.kniffel.model.dto.ScoreCrudDTO;
import de.coin.kniffel.repository.ScoreRepository;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ScoreCrudController implements Initializable {

    public TableView<ScoreCrudDTO> scoreTableView;
    public TableColumn<ScoreCrudDTO, Integer> gameIdColumn;
    public TableColumn<ScoreCrudDTO, Integer> playerIdColumn;
    public TableColumn<ScoreCrudDTO, String> playerNameColumn;
    public TableColumn<ScoreCrudDTO, String> gameInfoColumn;
    public TableColumn<ScoreCrudDTO, Integer> scoreColumn;
    public TableColumn<ScoreCrudDTO, Double> contributionColumn;

    private final ScoreRepository repository = new ScoreRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameIdColumn.setCellValueFactory(new PropertyValueFactory<>("gameId"));
        playerIdColumn.setCellValueFactory(new PropertyValueFactory<>("playerId"));
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        gameInfoColumn.setCellValueFactory(new PropertyValueFactory<>("gameInfo"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        contributionColumn.setCellValueFactory(new PropertyValueFactory<>("contribution"));

        scoreTableView.setItems(FXCollections.observableArrayList(repository.findAllForCrud()));
    }
}
