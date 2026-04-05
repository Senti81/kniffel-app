package de.coin.kniffel.controller.crud;

import de.coin.kniffel.model.Game;
import de.coin.kniffel.model.Player;
import de.coin.kniffel.model.dto.GameScoreViewDTO;
import de.coin.kniffel.repository.GameRepository;
import de.coin.kniffel.repository.PlayerRepository;
import de.coin.kniffel.repository.ScoreRepository;
import de.coin.kniffel.util.DateTimeUtils;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.util.*;

public class GameCrudController extends AbstractCrudController<GameScoreViewDTO> implements Initializable {

    public TableView<GameScoreViewDTO> gameTableView;
    public TableColumn<GameScoreViewDTO, Integer> yearColumn;
    public TableColumn<GameScoreViewDTO, Integer> numberColumn;
    public TableColumn<GameScoreViewDTO, String> dateColumn;

    private final GameRepository gameRepository = new GameRepository();
    private final PlayerRepository playerRepository = new PlayerRepository();
    private final ScoreRepository scoreRepository = new ScoreRepository();
    private List<Player> players;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        gameTableView.setEditable(true);
        dateColumn.setEditable(true);
        dateColumn.setCellFactory(createDateEditingCell());

        players = playerRepository.findAll();
        createPlayerColumns();
        loadGamesWithScores();
    }

    private Callback<TableColumn<GameScoreViewDTO, String>, TableCell<GameScoreViewDTO, String>> createDateEditingCell() {
        return column -> new DateEditingCell(gameRepository, this::refreshTable);
    }

    private void createPlayerColumns() {
        for (Player player : players) {
            String playerName = player.getPlayerName();
            TableColumn<GameScoreViewDTO, Number> playerColumn = new TableColumn<>(playerName);
            playerColumn.setPrefWidth(100);
            final String name = playerName;
            playerColumn.setCellValueFactory(cellData -> {
                Integer score = cellData.getValue().getScoreForPlayer(name);
                return new javafx.beans.property.SimpleIntegerProperty(score != null ? score : 0);
            });
            gameTableView.getColumns().add(playerColumn);
        }
    }

    private void loadGamesWithScores() {
        List<Game> games = gameRepository.findAll();
        List<GameScoreViewDTO> gameScoreViews = new ArrayList<>();

        for (Game game : games) {
            Map<String, Integer> playerScores = new HashMap<>();

            List<de.coin.kniffel.model.dto.ScoreCrudDTO> scores = scoreRepository.findAllForCrud();
            for (de.coin.kniffel.model.dto.ScoreCrudDTO score : scores) {
                if (score.gameIdProperty().getValue() == game.getGameId()) {
                    playerScores.put(score.playerNameProperty().getValue(), score.scoreProperty().getValue());
                }
            }

            for (Player player : players) {
                playerScores.putIfAbsent(player.getPlayerName(), 0);
            }

            gameScoreViews.add(new GameScoreViewDTO(
                    game.getGameId(),
                    game.getYear(),
                    game.getNr(),
                    game.getDate(),
                    playerScores
            ));
        }

        itemList = FXCollections.observableArrayList(gameScoreViews);
        gameTableView.setItems(itemList);
    }

    public void handleDelete() {
        GameScoreViewDTO selected = gameTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Kein Spiel ausgewählt");
            return;
        }

        Game game = new Game();
        game.setGameId(selected.getGameId());
        gameRepository.delete(game);
        refreshTable();
    }

    @Override
    protected void refreshTable() {
        itemList.clear();
        loadGamesWithScores();
    }

    public void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showDateErrorAlert() {
        showAlert(Alert.AlertType.ERROR, "Ungültiges Datum", "Format: dd.MM.yyyy (z.B. 05.04.2026)");
    }

    private static class DateEditingCell extends TableCell<GameScoreViewDTO, String> {
        private final TextField textField;
        private final GameRepository gameRepository;
        private final Runnable onUpdate;

        public DateEditingCell(GameRepository gameRepository, Runnable onUpdate) {
            this.gameRepository = gameRepository;
            this.onUpdate = onUpdate;
            this.textField = new TextField();
            this.textField.setAlignment(Pos.CENTER_RIGHT);
            this.textField.setPrefWidth(100);
            this.textField.setOnAction(event -> commitEdit(textField.getText()));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(null);
                setText(item);
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            String currentValue = getItem();
            if (currentValue != null) {
                textField.setText(currentValue);
                setGraphic(textField);
                setText(null);
                textField.requestFocus();
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setGraphic(null);
            String currentValue = getItem();
            setText(currentValue != null ? currentValue : "");
        }

        @Override
        public void commitEdit(String newValue) {
            String oldValue = getItem();

            if (newValue == null || newValue.equals(oldValue)) {
                cancelEdit();
                return;
            }

            if (!DateTimeUtils.isValidDate(newValue)) {
                showDateError();
                cancelEdit();
                return;
            }

            GameScoreViewDTO game = getTableView().getItems().get(getIndex());
            gameRepository.updateDate(game.getGameId(), DateTimeUtils.parseDate(newValue));
            game.setDateFromString(DateTimeUtils.formatDate(DateTimeUtils.parseDate(newValue)));

            super.commitEdit(newValue);
            setGraphic(null);
            setText(newValue);
            onUpdate.run();
        }

        private void showDateError() {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ungültiges Datum");
            alert.setContentText("Format: dd.MM.yyyy (z.B. 05.04.2026)");
            alert.showAndWait();
        }
    }
}
