package de.coin.kniffel.controller.crud;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.service.GameService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GameCrudController extends AbstractCrudController<Game> implements Initializable {

    public TableView<Game> gameTableView;
    public TableColumn<Game, Integer> idColumn;
    public TableColumn<Game, Integer> numberColumn;
    public TableColumn<Game, Integer> yearColumn;
    public TableColumn<Game, LocalDate> dateColumn;
    public Button backButton;
    public Button deleteButton;

    private final GameService service = new GameService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("gameId"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        itemList = FXCollections.observableArrayList(service.getAllGames());
        gameTableView.setItems(itemList);

        backButton.setOnAction(e -> ((javafx.stage.Stage) backButton.getScene().getWindow()).close());
    }

    public void handleDelete(ActionEvent actionEvent) {
        Game game = gameTableView.getSelectionModel().getSelectedItem();
        if (game == null) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Kein Spiel ausgewählt");
        } else {
            service.deleteGame(game);
            refreshTable();
        }
    }

    @Override
    protected void refreshTable() {
        itemList.clear();
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
