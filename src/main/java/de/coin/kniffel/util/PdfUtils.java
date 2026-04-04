package de.coin.kniffel.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import de.coin.kniffel.model.dto.GameResultDTO;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PdfUtils {

    private static final float[] METADATA_TABLE_WIDTHS = {1, 1, 2, 1, 1, 1};
    private static final float[] DATA_TABLE_WIDTHS = {1, 1, 1, 1, 1, 1, 1};
    private static final String[] TABLE_HEADERS = {"Name", "Punkte", "Platz", "Wert", "Ges. Wert", "Ges. Punkte", "Ges. Pos."};

    public static void createPdf(
            List<GameResultDTO> gameResultDTOTableView,
            List<GameResultDTO> gameResultDTOTableView2,
            List<GameResultDTO> seasonResultDTOTableView,
            List<GameResultDTO> seasonResultDTOTableView2,
            int year,
            List<Integer> gameNumbers,
            LocalDate gameDate) {
        createPdf(gameResultDTOTableView, gameResultDTOTableView2, seasonResultDTOTableView,
                seasonResultDTOTableView2, year, gameNumbers, gameDate, true);
    }

    public static void createPdf(
            List<GameResultDTO> gameResultDTOTableView,
            List<GameResultDTO> gameResultDTOTableView2,
            List<GameResultDTO> seasonResultDTOTableView,
            List<GameResultDTO> seasonResultDTOTableView2,
            int year,
            List<Integer> gameNumbers,
            LocalDate gameDate,
            boolean showDialog) {
        log.info("Creating PDF...");
        try {
            Document document = new Document(PageSize.A4.rotate());
            String fileOutputPath = "data/" + year + "_" + gameNumbers.getFirst() + "_" + gameNumbers.getLast() +  ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileOutputPath));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.ITALIC, BaseColor.DARK_GRAY);
            Font headerCellFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font dataCellFont = new Font(Font.FontFamily.HELVETICA, 14, Font.NORMAL);

            PdfPTable metaDataTable = createMetaDataTable(year, gameNumbers.getFirst(), gameDate, titleFont);
            PdfPTable metaDataTable2 = createMetaDataTable(year, gameNumbers.getLast(), gameDate.plusDays(1), titleFont);

            PdfPTable dataTable1 = createDataTable(headerCellFont);
            fillTableWithCombinedDataFromLists(dataTable1, seasonResultDTOTableView, gameResultDTOTableView, dataCellFont);

            PdfPTable dataTable2 = createDataTable(headerCellFont);
            fillTableWithCombinedDataFromLists(dataTable2, seasonResultDTOTableView2, gameResultDTOTableView2, dataCellFont);

            document.add(new Phrase("\n"));
            document.add(metaDataTable);
            document.add(dataTable1);
            document.add(new Phrase("\n\n"));
            document.add(metaDataTable2);
            document.add(dataTable2);

            log.info("PDF created successfully: {}", fileOutputPath);
            if (showDialog) {
                DialogUtils.showConfirmationDialogWithOk("PDF erfolgreich exportiert");
            }
            document.close();
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error creating PDF: {}", e.getMessage());
        }
    }

    private static PdfPTable createMetaDataTable(int year, int gameNumber, LocalDate date, Font font) {
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
            headerCell.setPadding(5);
            headerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(headerCell);
        }
        return table;
    }

    private static void fillTableWithCombinedDataFromLists(
            PdfPTable table,
            List<GameResultDTO> seasonResults,
            List<GameResultDTO> gameResults,
            Font dataFont) {
        // Create a map from game results for quick lookups by player name
        Map<String, GameResultDTO> gameResultsMap = new HashMap<>();
        gameResults.forEach(gameResult ->
                gameResultsMap.put(gameResult.getPlayerName().get(), gameResult));

        // Sort the season results by position and process each
        seasonResults.stream()
                .sorted(Comparator.comparingInt(seasonResult -> seasonResult.getPosition().get()))
                .forEach(seasonResult -> {
                    String playerName = seasonResult.getPlayerName().get();
                    GameResultDTO gameResult = gameResultsMap.getOrDefault(playerName, null);

                    table.addCell(createCell(playerName, dataFont, PdfPCell.ALIGN_LEFT)); // Player name
                    table.addCell(createCell(
                            gameResult != null ? String.valueOf(gameResult.getFinalScore().get()) : "-",
                            dataFont,
                            PdfPCell.ALIGN_RIGHT)); // Game final score
                    table.addCell(createCell(
                            gameResult != null ? String.valueOf(gameResult.getPosition().get()) : "-",
                            dataFont,
                            PdfPCell.ALIGN_CENTER)); // Game position
                    table.addCell(createCell(
                            gameResult != null ? String.format("%.2f €", gameResult.getContribution().get()) : "-",
                            dataFont,
                            PdfPCell.ALIGN_RIGHT)); // Game contribution
                    table.addCell(createCell(
                            String.format("%.2f €", seasonResult.getContribution().get()),
                            dataFont,
                            PdfPCell.ALIGN_RIGHT)); // Season contribution
                    table.addCell(createCell(
                            String.valueOf(seasonResult.getFinalScore().get()),
                            dataFont,
                            PdfPCell.ALIGN_RIGHT)); // Season final score
                    table.addCell(createCell(
                            String.valueOf(seasonResult.getPosition().get()),
                            dataFont,
                            PdfPCell.ALIGN_CENTER)); // Season position
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
