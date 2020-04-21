package dekad.controllers;

import dekad.core.DekadApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class App extends VBox {

    @SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
    private final transient DekadApp app;

    @FXML
    private transient HBox container;

    public App(final DekadApp app) {

        super();

        this.app = app;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/app.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            app.setAppController(this);

            this.getChildren().add(0, new Menu(app));

            container.getChildren().add(new FunctionsPane(app));
            container.getChildren().add(new Graph(app));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}