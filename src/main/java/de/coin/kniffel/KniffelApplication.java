package de.coin.kniffel;

import java.util.Objects;

import de.coin.kniffel.util.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KniffelApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        launch();
    }
}