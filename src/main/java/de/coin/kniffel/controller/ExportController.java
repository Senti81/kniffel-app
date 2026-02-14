package de.coin.kniffel.controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.coin.kniffel.service.GameService;
import de.coin.kniffel.service.ScoreService;
import de.coin.kniffel.util.PdfUtils;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportController implements Initializable {

    private final GameService gameService = new GameService();
    private final ScoreService scoreService = new ScoreService();

    public ComboBox<Integer> comboBoxYear;
    public ComboBox<Map.Entry<Integer, List<Integer>>> comboBoxGames;
    public Button btnExport;

    private int selectedYear = 0;
    private List<Integer> selectedGameNumbers = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Integer> allGameYears = gameService.getAllGameYears();
        comboBoxYear.getItems().addAll(allGameYears);

        comboBoxGames.setCellFactory(listView -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Map.Entry<Integer, List<Integer>> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Customize the text for each entry
                    setText("Spiele: " + item.getValue().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(" + ")));
                }
            }
        });

        comboBoxGames.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Map.Entry<Integer, List<Integer>> item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Spiele: " + item.getValue().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(" + ")));
                }
            }
        });

        comboBoxYear.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedYear = newValue;
                List<Integer> allGameNumbersByYear = gameService.getAllGameNumbersByYear(newValue);
                Map<Integer, List<Integer>> allGameNumbersMapped = mapGameNumbersToPairs(allGameNumbersByYear);

                comboBoxGames.getItems().clear();
                comboBoxGames.getItems().addAll(allGameNumbersMapped.entrySet());

            }
        });

        comboBoxGames.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                selectedGameNumbers = newValue.getValue();
                log.info("Selected game numbers: {}, {}", selectedGameNumbers.get(0), selectedGameNumbers.get(1));

            }
        });
    }

    /**
     * Maps a list of game numbers into a Map<Integer, List<Integer>> such that:
     * 1 -> [1, 2], 2 -> [3, 4], etc.
     *
     * @param gameNumbers The list of all game numbers
     * @return A map in the specified format
     */
    private Map<Integer, List<Integer>> mapGameNumbersToPairs(List<Integer> gameNumbers) {
        return IntStream.range(0, (int) Math.ceil(gameNumbers.size() / 2.0))
                .boxed()
                .collect(Collectors.toMap(
                        i -> i + 1,
                        i -> gameNumbers.subList(i * 2, Math.min((i + 1) * 2, gameNumbers.size()))
                ));
    }

    public void export() {

//        log.info("Printing the following information to PDF: {} {} {} {}", listGameResults, listGameResults2, listSeasonResults, listSeasonResults2);
//        PdfUtils.createPdf(listGameResults, listGameResults2, listSeasonResults, listSeasonResults2, selectedYear, selectedGameNumber, selectedGameDate);
    }
}
