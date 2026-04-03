package de.coin.kniffel.controller.crud;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.service.GameService;
import de.coin.kniffel.util.DateTimeUtils;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class GameCrudController extends AbstractCrudController<Game> implements Initializable {

    public TableView<Game> gameTableView;
    public TableColumn<Game, Integer> idColumn;
    public TableColumn<Game, Integer> numberColumn;
    public TableColumn<Game, Integer> yearColumn;
    public TableColumn<Game, LocalDate> dateColumn;
    public TableColumn<Game, LocalDateTime> createdAtColumn;
    public TableColumn<Game, LocalDateTime> updatedAtColumn;

    private final GameService service = new GameService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("gameId"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        createdAtColumn.setCellFactory(col -> new javafx.scene.control.TableCell<Game, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(item != null ? DateTimeUtils.DATE_TIME_FORMATTER.format(item) : "");
            }
        });

        updatedAtColumn.setCellFactory(col -> new javafx.scene.control.TableCell<Game, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                setText(item != null ? DateTimeUtils.DATE_TIME_FORMATTER.format(item) : "");
            }
        });

        itemList = FXCollections.observableArrayList(service.getAllGames());
        gameTableView.setItems(itemList);
    }

    public void handleDelete() {
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
