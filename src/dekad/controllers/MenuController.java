package dekad.controllers;

import dekad.core.App;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        App.updateControllers();

        System.out.println("Menu Controller loaded.");

    }

    public void exit() {
        System.exit(0);
    }

}
