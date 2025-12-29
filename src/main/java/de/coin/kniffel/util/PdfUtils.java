package de.coin.kniffel.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.coin.kniffel.model.dto.GameResultDTO;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PdfUtils {

    public static void createPdf(
            TableView<GameResultDTO> gameResultDTOTableView,
            TableView<GameResultDTO> seasonResultDTOTableView,
            int year,
            int gameNumber,
            LocalDate gameDate) {
        log.info("Creating PDF...");
        try {
            Document document = new Document(PageSize.A4.rotate());

            String fileOutputPath = "data/" + year + "-" + gameNumber + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileOutputPath));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.ITALIC, BaseColor.DARK_GRAY);
            Font headerCellFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font dataCellFont = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL);

            PdfPTable metaDataTable = new PdfPTable(6);
            metaDataTable.setWidthPercentage(100);
            metaDataTable.setWidths(new float[]{1, 1, 2, 1, 1, 1});
            metaDataTable.addCell(createCell("Datum", titleFont, PdfPCell.ALIGN_LEFT));
            metaDataTable.addCell(createCell(DateTimeUtils.formatDate(gameDate), titleFont, PdfPCell.ALIGN_RIGHT));
            metaDataTable.addCell(createCell("Kniffelliga ", titleFont, PdfPCell.ALIGN_LEFT));
            metaDataTable.addCell(createCell(String.valueOf(year), titleFont, PdfPCell.ALIGN_CENTER));
            metaDataTable.addCell(createCell("Spiel", titleFont, PdfPCell.ALIGN_CENTER));
            metaDataTable.addCell(createCell(String.valueOf(gameNumber), titleFont, PdfPCell.ALIGN_CENTER));

            // Define the columns for the combined table
            PdfPTable table = new PdfPTable(7); // Adjust for 7 columns
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 1, 1, 1, 1, 1, 1}); // Adjust column widths


            // Add table headers
            String[] headers = {"Name", "Punkte", "Platz", "Wert", "Ges. Wert", "Ges. Punkte", "Ges. Pos."};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerCellFont));
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell.setPadding(5);
                headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(headerCell);
            }

            // Create a mapping of game results by name for easy lookup
            Map<String, GameResultDTO> gameResultsMap = new HashMap<>();
            gameResultDTOTableView.getItems().forEach(gameResult -> gameResultsMap.put(gameResult.getPlayerName().get(), gameResult));

            // Merge data: Use season data to drive the table and enrich with game data
            seasonResultDTOTableView.getItems().stream()
                    .sorted(Comparator.comparingInt(item -> item.getPosition().get())) // Sort by SeasonPlatz
                    .forEach(seasonResult -> {
                        String playerName = seasonResult.getPlayerName().get();
                        GameResultDTO gameResult = gameResultsMap.getOrDefault(playerName, null);


                        // Add the combined data into the PDF table
                        PdfPCell nameCell = createCell(playerName, dataCellFont, PdfPCell.ALIGN_LEFT);

                        PdfPCell gameScoreCell = createCell(
                                gameResult != null ? String.valueOf(gameResult.getFinalScore().get()) : "-",
                                dataCellFont,
                                PdfPCell.ALIGN_RIGHT
                        );

                        PdfPCell gamePositionCell = createCell(
                                gameResult != null ? String.valueOf(gameResult.getPosition().get()) : "-",
                                dataCellFont,
                                PdfPCell.ALIGN_CENTER
                        );

                        PdfPCell gameContributionCell = createCell(
                                gameResult != null ? String.format("%.2f €", gameResult.getContribution().get()) : "-",
                                dataCellFont,
                                PdfPCell.ALIGN_RIGHT
                        );

                        PdfPCell seasonContributionCell = createCell(
                                String.format("%.2f €", seasonResult.getContribution().get()),
                                dataCellFont,
                                PdfPCell.ALIGN_RIGHT
                        );

                        PdfPCell seasonScoreCell = createCell(
                                String.valueOf(seasonResult.getFinalScore().get()),
                                dataCellFont,
                                PdfPCell.ALIGN_RIGHT
                        );

                        PdfPCell seasonPositionCell = createCell(
                                String.valueOf(seasonResult.getPosition().get()),
                                dataCellFont,
                                PdfPCell.ALIGN_CENTER
                        );

                        table.addCell(nameCell);
                        table.addCell(gameScoreCell);
                        table.addCell(gamePositionCell);
                        table.addCell(gameContributionCell);
                        table.addCell(seasonContributionCell);
                        table.addCell(seasonScoreCell);
                        table.addCell(seasonPositionCell);
                    });

            // Add table to the document
            document.add(metaDataTable);
            document.add(table);
            document.close();

            // Alert user of successful PDF creation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF created");
            alert.setHeaderText(null);
            alert.setContentText("PDF erfolgreich gespeichert unter: " + fileOutputPath);
            alert.showAndWait();

            log.info("PDF created successfully");
        } catch (FileNotFoundException | com.itextpdf.text.DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    // Helper method to create cells
    private static PdfPCell createCell (String content, Font font, int hAlignment){
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(hAlignment);
        cell.setVerticalAlignment(com.itextpdf.text.Element.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }
}
