package de.coin.kniffel.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DialogUtils {

    public static Optional<ButtonType> showConfirmationDialog(String content) {
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Bestätigen");
        confirmationDialog.setHeaderText("Sind die folgenden Eingaben korrekt?");
        confirmationDialog.setContentText(content);

        confirmationDialog.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        return confirmationDialog.showAndWait();
    }
}
