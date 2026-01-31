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

    private static final float[] METADATA_TABLE_WIDTHS = {1, 1, 2, 1, 1, 1};
    private static final float[] DATA_TABLE_WIDTHS = {1, 1, 1, 1, 1, 1, 1};
    private static final String[] TABLE_HEADERS = {"Name", "Punkte", "Platz", "Wert", "Ges. Wert", "Ges. Punkte", "Ges. Pos."};

    public static void createPdf(
            TableView<GameResultDTO> gameResultDTOTableView1,
            TableView<GameResultDTO> gameResultDTOTableView2,
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

            // Create and add metadata tables
            PdfPTable metaDataTable = createMetaDataTable(year, gameNumber, gameDate, titleFont);
            PdfPTable metaDataTable2 = createMetaDataTable(year, gameNumber + 1, gameDate.plusDays(1), titleFont);

            PdfPTable dataTable1 = createDataTable(headerCellFont);
            fillTableWithCombinedData(dataTable1, seasonResultDTOTableView, gameResultDTOTableView1, dataCellFont);

            PdfPTable dataTable2 = createDataTable(headerCellFont);
            fillTableWithCombinedData(dataTable2, seasonResultDTOTableView, gameResultDTOTableView2, dataCellFont);

            document.add(new Phrase("\n"));
            document.add(metaDataTable);
            document.add(dataTable1);
            document.add(new Phrase("\n\n"));
            document.add(metaDataTable2);
            document.add(dataTable2);

            log.info("PDF created successfully: {}", fileOutputPath);
            DialogUtils.showConfirmationDialogWithOk("PDF erfolgreich exportiert");
            document.close();
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error creating PDF: {}", e.getMessage());
        }
    }

    private static PdfPTable createMetaDataTable(int year, int gameNumber, LocalDate date, Font font) throws Exception {
        PdfPTable metaDataTable = new PdfPTable(METADATA_TABLE_WIDTHS);
        metaDataTable.setWidthPercentage(100);

        metaDataTable.addCell(createCell("Datum", font, PdfPCell.ALIGN_LEFT));
        metaDataTable.addCell(createCell(DateTimeUtils.formatDate(date), font, PdfPCell.ALIGN_RIGHT));
        metaDataTable.addCell(createCell("Kniffelliga", font, PdfPCell.ALIGN_LEFT));
        metaDataTable.addCell(createCell(String.valueOf(year), font, PdfPCell.ALIGN_CENTER));
        metaDataTable.addCell(createCell("Spiel", font, PdfPCell.ALIGN_CENTER));
        metaDataTable.addCell(createCell(String.valueOf(gameNumber), font, PdfPCell.ALIGN_CENTER));

        return metaDataTable;
    }

    private static PdfPTable createDataTable(Font headerFont) throws Exception {
        PdfPTable table = new PdfPTable(TABLE_HEADERS.length);
        table.setWidthPercentage(100);
        table.setWidths(DATA_TABLE_WIDTHS);

        for (String header : TABLE_HEADERS) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
            headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            headerCell.setPadding(5);
            headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(headerCell);
        }
        return table;
    }

    private static void fillTableWithCombinedData(
            PdfPTable table,
            TableView<GameResultDTO> seasonResultTable,
            TableView<GameResultDTO> gameResultTable,
            Font dataFont) {
        Map<String, GameResultDTO> gameResultsMap = new HashMap<>();
        gameResultTable.getItems().forEach(gameResult ->
                gameResultsMap.put(gameResult.getPlayerName().get(), gameResult));

        seasonResultTable.getItems().stream()
                .sorted(Comparator.comparingInt(seasonResult -> seasonResult.getPosition().get()))
                .forEach(seasonResult -> {
                    String playerName = seasonResult.getPlayerName().get();
                    GameResultDTO gameResult = gameResultsMap.getOrDefault(playerName, null);

                    table.addCell(createCell(playerName, dataFont, PdfPCell.ALIGN_LEFT));
                    table.addCell(createCell(gameResult != null ? String.valueOf(gameResult.getFinalScore().get()) : "-", dataFont, PdfPCell.ALIGN_RIGHT));
                    table.addCell(createCell(gameResult != null ? String.valueOf(gameResult.getPosition().get()) : "-", dataFont, PdfPCell.ALIGN_CENTER));
                    table.addCell(createCell(gameResult != null ? String.format("%.2f €", gameResult.getContribution().get()) : "-", dataFont, PdfPCell.ALIGN_RIGHT));
                    table.addCell(createCell(String.format("%.2f €", seasonResult.getContribution().get()), dataFont, PdfPCell.ALIGN_RIGHT));
                    table.addCell(createCell(String.valueOf(seasonResult.getFinalScore().get()), dataFont, PdfPCell.ALIGN_RIGHT));
                    table.addCell(createCell(String.valueOf(seasonResult.getPosition().get()), dataFont, PdfPCell.ALIGN_CENTER));
                });
    }

    private static PdfPCell createCell(String content, Font font, int horizontalAlignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
        cell.setPadding(5);
        return cell;
    }

}
