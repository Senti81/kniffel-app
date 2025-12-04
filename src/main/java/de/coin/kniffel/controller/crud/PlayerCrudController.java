package de.coin.kniffel.controller.crud;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import de.coin.kniffel.model.Player;
import de.coin.kniffel.service.PlayerService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerCrudController extends AbstractCrudController<Player> implements Initializable {

    public TableView<Player> playerTableView;
    public TableColumn<Player, Integer> idColumn;
    public TableColumn<Player, String> nameColumn;
    public TableColumn<Player, LocalDate> createdAtColumn;
    public TableColumn<Player, LocalDate> updatedAtColumn;
    public TextField nameField;
    public Button backButton;

    private final PlayerService service = new PlayerService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("playerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));

        itemList = FXCollections.observableArrayList(service.getAllPlayers());
        playerTableView.setItems(itemList);

        backButton.setOnAction(e -> ((javafx.stage.Stage) backButton.getScene().getWindow()).close());
    }

    public void handleAddPlayer(ActionEvent actionEvent) {
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Name darf nicht leer sein.");
            return;
        }
        service.addNewPlayer(nameField.getText());
        refreshTable();
    }

    public void handleUpdatePlayer(ActionEvent actionEvent) {
        Player player = playerTableView.getSelectionModel().getSelectedItem();
        if (player == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Kein Spieler ausgewählt.");
            return;
        }
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Name darf nicht leer sein.");
        }

        String newName = nameField.getText();
        service.updatePlayer(player, newName);
        refreshTable();
    }

    public void handleDeletePlayer(ActionEvent actionEvent) {
        Player player = playerTableView.getSelectionModel().getSelectedItem();
        if (player == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Kein Spieler ausgewählt.");
        } else {
            service.deletePlayer(player);
            refreshTable();
        }
    }

    @Override
    protected void refreshTable() {
        itemList.clear();
        itemList.addAll(service.getAllPlayers());
    }
}
