package dekad.core;

import dekad.controllers.*;
import dekad.models.Settings;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Main Application class
 */
public class DekadApp extends Application {

    // Main Controllers
    private App appController;
    private Menu menu;
    private FunctionsPane functionsPane;
    private Graph graph;

    // Settings
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private transient Settings settings;

    @Override
    public void start(final Stage primaryStage) throws IOException {

        // Try to load the existing settings or generate them
        final String settingsFile = Paths.get(System.getProperty("user.home") + "\\dekad.xml").toAbsolutePath().normalize().toString();
        settings = Settings.readOrGenerate(settingsFile);

        // Load the css
        final String css = this.getClass().getResource("/dekad/views/style.css").toExternalForm();

        // Create the root, load it's css and *magic* let's show the app
        final Parent root = new App(this);
        root.getStylesheets().add(css);
        primaryStage.setTitle("Dekad");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * Get the app controller
     * @return the App Controller
     */
    public App getAppController() {
        return appController;
    }

    /**
     * Get menu controller
     * @return the Menu Controller
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Get the functionspane controller
     * @return the FunctionsPane Controller
     */
    public FunctionsPane getFunctionsPane() {
        return functionsPane;
    }

    /**
     * Get the graph controller
     * @return the Graph Controller
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Set the app controller into the app
     * @param appController App Controller
     */
    public void setAppController(final App appController) {
        this.appController = appController;
    }

    /**
     * Set the menu controller into the app
     * @param menu Menu Controller
     */
    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    /**
     * Set the functionpane controller into the app
     * @param functionsPane FunctionsPane Controller
     */
    public void setFunctionsPane(final FunctionsPane functionsPane) {
        this.functionsPane = functionsPane;
    }

    /**
     * Set the graph controller into the app
     * @param graph Graph Controller
     */
    public void setGraph(final Graph graph) {
        this.graph = graph;
    }

    /**
     * Get the settings
     * @return The Application's settings
     */
    public Settings settings() {

        return settings;

    }

}
