package dekad.core;

import dekad.controllers.GraphController;
import dekad.models.GraphConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("PMD.ShortClassName")
public class App extends Application {

    public static GraphController graphController;
    public static GraphConfig graphConfig = new GraphConfig();

    @Override
    public void start(final Stage primaryStage) throws IOException {
        final Parent root = FXMLLoader.load(getClass().getResource("/dekad/views/app.fxml"));

        primaryStage.setTitle("Dekad");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch(args);
    }

    public static void updateControllers() {

        if(graphController == null) graphController = GraphController.graphController;

    }

}
