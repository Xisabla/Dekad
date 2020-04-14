package dekad.controllers;

import dekad.core.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    public CheckMenuItem toggleAutoYBoundsCheckbox;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        App.updateControllers();

        toggleAutoYBoundsCheckbox.setSelected(App.graphConfig.hasAutoYBounds());

        System.out.println("Menu Controller loaded.");

    }

    public void exit() {
        System.exit(0);
    }

    public void toggleAutoYBounds() {

        App.graphConfig.setAutoYBounds(toggleAutoYBoundsCheckbox.isSelected());
        App.graphController.update();

    }
}
