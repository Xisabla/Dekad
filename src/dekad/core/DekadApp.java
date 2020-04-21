package dekad.core;

import dekad.controllers.*;
import dekad.models.Settings;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Paths;

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

        // Settings
        final String defaultSettingsFile = getClass().getResource("/dekad/data/settings.xml").getFile();
        final String settingsFile = Paths.get("./settings.xml").toAbsolutePath().normalize().toString();

        settings = Settings.readOrGenerate(settingsFile, defaultSettingsFile);

        // Scene
        final Parent root = new App(this);
        primaryStage.setTitle("Dekad");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    public static void main(final String[] args) {
        launch(args);
    }

    // Controllers
    public App getAppController() {
        return appController;
    }

    public Menu getMenu() {
        return menu;
    }

    public FunctionsPane getFunctionsPane() {
        return functionsPane;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setAppController(final App appController) {
        this.appController = appController;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public void setFunctionsPane(final FunctionsPane functionsPane) {
        this.functionsPane = functionsPane;
    }

    public void setGraph(final Graph graph) {
        this.graph = graph;
    }

    // Get settings
    public Settings settings() {

        return settings;

    }

}
