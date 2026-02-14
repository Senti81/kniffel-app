package de.coin.kniffel.controller;

import de.coin.kniffel.model.dto.GameResultDTO;
import de.coin.kniffel.service.GameService;
import de.coin.kniffel.service.ScoreService;
import de.coin.kniffel.util.PdfUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ExportController implements Initializable {

    private final GameService gameService = new GameService();
    private final ScoreService scoreService = new ScoreService();

    public List<GameResultDTO> listGameResults;
    public List<GameResultDTO> listGameResults2;
    public List<GameResultDTO> listSeasonResults;
    public List<GameResultDTO> listSeasonResults2;

    public ComboBox<Integer> comboBoxYear;
    public ComboBox<Map.Entry<Integer, List<Integer>>> comboBoxGames;

    private int selectedYear = 0;
    private List<Integer> selectedGameNumbers = null;
    private LocalDate selectedGameDate;

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
                selectedGameDate = gameService.getGameDateByYearAndNumber(selectedYear, selectedGameNumbers.getFirst());

                log.info("Selected game numbers: {}, {}", selectedGameNumbers.get(0), selectedGameNumbers.get(1));

                listGameResults = scoreService.getGameResultsByYearAndGameNumber(selectedYear, selectedGameNumbers.getFirst());
                listGameResults2 = scoreService.getGameResultsByYearAndGameNumber(selectedYear, selectedGameNumbers.getLast());
                listSeasonResults = scoreService.getSeasonResultsByYear(selectedYear, selectedGameNumbers.getFirst());
                listSeasonResults2 = scoreService.getSeasonResultsByYear(selectedYear, selectedGameNumbers.getLast());
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
        log.info("Printing the following information to PDF: {} {} {} {}", listGameResults, listGameResults2, listSeasonResults, listSeasonResults2);
        PdfUtils.createPdf(listGameResults, listGameResults2, listSeasonResults, listSeasonResults2, selectedYear, selectedGameNumbers, selectedGameDate);
    }
}
