package de.coin.kniffel.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DialogUtils {

    public static void showHelpDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        alert.setTitle("Hilfe");
        alert.setHeaderText("So funktioniert's:");
        alert.showAndWait();
    }

    public static Optional<ButtonType> showConfirmationDialog(String content) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Bestätigen");
        confirmationDialog.setHeaderText("Sind die folgenden Eingaben korrekt?");
        confirmationDialog.setContentText(content);

        confirmationDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        return confirmationDialog.showAndWait();
    }

    public static Optional<ButtonType> showConfirmationDialogWithOk(String content) {
        Alert confirmationDialog = new Alert(Alert.AlertType.INFORMATION);
        confirmationDialog.setContentText(content);
        confirmationDialog.getButtonTypes().setAll(ButtonType.OK);
        return confirmationDialog.showAndWait();
    }

    public static Optional<ButtonType> showConfirmationDialogWithOkAndCancel(String content) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setContentText(content);
        confirmationDialog.getButtonTypes().setAll(ButtonType.OK);
        confirmationDialog.getButtonTypes().add(ButtonType.CANCEL);
        return confirmationDialog.showAndWait();
    }

    public static void showResultDialog(boolean wasSuccessful) {
        Alert dialog = new Alert(wasSuccessful ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        dialog.setHeaderText(wasSuccessful ? "Erfolg" : "Fehler");
        dialog.getButtonTypes().setAll(ButtonType.OK);
        dialog.show();
    }

    public static void showErrorDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setHeaderText("Hoppla, da ist was schief gelaufen");
        dialog.setContentText(message);
        dialog.getButtonTypes().setAll(ButtonType.OK);
        dialog.show();
    }
}
