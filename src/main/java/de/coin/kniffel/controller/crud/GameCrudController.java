package de.coin.kniffel.controller.crud;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import de.coin.kniffel.model.dto.GameDTO;
import de.coin.kniffel.service.GameService;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GameCrudController implements Initializable {

    public TableView<GameDTO> gameTableView;
    public TableColumn<GameDTO, Integer> numberColumn;
    public TableColumn<GameDTO, Integer> yearColumn;
    public TableColumn<GameDTO, LocalDate> dateColumn;
    public Button backButton;

    private final GameService service = new GameService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("gameNumber"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("gameYear"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("gameDate"));
        service.getAllGames().forEach(gameDTO -> gameTableView.getItems().add(gameDTO));
        backButton.setOnAction(e -> ((javafx.stage.Stage) backButton.getScene().getWindow()).close());
    }
}
