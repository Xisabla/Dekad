package dekad.controllers;

import dekad.core.DekadApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * Main Controller of the Application,
 *  will instantiate and call all the other controllers
 */
public class App extends VBox {

    @SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
    /**
     * All controllers a containing the instance of the DekadApp,
     *  it allows them to access to the other controllers and the settings
     *  without using static members
     */
    private final transient DekadApp app;

    /**
     * HBox container, main JavaFX container of all the inner elements, Menu excepted
     */
    @FXML
    private transient HBox container;

    /**
     * Load the FXML resource and append the children (Menu, FunctionsPane, Graph)
     * @param app The main DekadApp object
     */
    public App(final DekadApp app) {

        super();

        this.app = app;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/app.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        // Load the resource and bind the root and controller
        try {
            fxmlLoader.load();

            app.setAppController(this);

            // Append the children
            this.getChildren().add(0, new Menu(app));

            container.getChildren().add(new FunctionsPane(app));
            container.getChildren().add(new Graph(app));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}