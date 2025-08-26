module de.coin.kniffel {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jdk.jdi;
    requires org.slf4j;
    requires static lombok;
    requires java.desktop;
    requires itextpdf;

    opens de.coin.kniffel.model to javafx.fxml;
    opens de.coin.kniffel.controller to javafx.fxml;

    exports de.coin.kniffel.model;
    exports de.coin.kniffel.model.dto;
    exports de.coin.kniffel.controller;
    exports de.coin.kniffel;
    exports de.coin.kniffel.service;
    opens de.coin.kniffel.service to javafx.fxml;
    exports de.coin.kniffel.controller.crud;
    opens de.coin.kniffel.controller.crud to javafx.fxml;
}