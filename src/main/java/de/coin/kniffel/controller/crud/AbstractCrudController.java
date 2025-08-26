package de.coin.kniffel.controller.crud;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public abstract class AbstractCrudController<T> {

    protected ObservableList<T> itemList;
    protected abstract void refreshTable();

    protected void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
