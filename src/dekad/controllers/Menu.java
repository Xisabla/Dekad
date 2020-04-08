package dekad.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(location);
    }

    public void exit() {
        System.exit(0);
    }

}
