package de.coin.kniffel.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
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

    public static void createPdf(TableView<GameResultDTO> gameResultDTOTableView, int year, int gameNumber) {
        log.info("Creating PDF...");
        try {
            Document document = new Document();

            String fileOutputPath = "data/" + year + "-" + gameNumber + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileOutputPath));
            document.open();

            Phrase headerPhrase = new Phrase("Jahr: " + year + " | Spiel: " + gameNumber);
            document.add(headerPhrase);

            int numColumns = gameResultDTOTableView.getColumns().size();
            PdfPTable table = new PdfPTable(numColumns);


            gameResultDTOTableView.getColumns().forEach(column -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(2);
                header.setPadding(5);
                header.setPhrase(new Phrase(column.getText()));
                table.addCell(header);
            });

            gameResultDTOTableView.getItems().forEach(item -> {
                PdfPCell positionCell = new PdfPCell(new Phrase(String.valueOf(item.getPosition().get())));
                positionCell.setPadding(5);
                table.addCell(positionCell);

                PdfPCell playerNameCell = new PdfPCell(new Phrase(String.valueOf(item.getPlayerName().get())));
                playerNameCell.setPadding(5);
                table.addCell(playerNameCell);

                PdfPCell finalScoreCell = new PdfPCell(new Phrase(String.valueOf(item.getFinalScore().get())));
                finalScoreCell.setPadding(5);
                table.addCell(finalScoreCell);

                PdfPCell contributionCell = new PdfPCell(new Phrase(String.format("%.2f €", item.getContribution().get())));
                contributionCell.setPadding(5);
                table.addCell(contributionCell);
            });

            document.add(table);
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF erstellt");
            alert.setHeaderText(null);
            alert.setContentText("PDF wurde erfolgreich gespiechert unter: " + fileOutputPath);
            alert.showAndWait();

            log.info("PDF created successfully");
        } catch (FileNotFoundException | com.itextpdf.text.DocumentException e) {
            throw new RuntimeException(e);
        }

    }

}
