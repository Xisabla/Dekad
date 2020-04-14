package dekad.controllers;

import dekad.core.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    public static MenuController menuController;

    @FXML
    public CheckMenuItem autoYBoundsCheckbox;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        menuController = this;
        App.updateControllers();

        autoYBoundsCheckbox.setSelected(App.graphConfig.hasAutoYBounds());

        System.out.println("Menu Controller loaded.");

    }

    public void exit() {
        System.exit(0);
    }

    public void toggleAutoYBounds() {

        App.graphConfig.setAutoYBounds(autoYBoundsCheckbox.isSelected());
        App.graphController.update();

    }

    public void forceAutoYBounds(boolean value) {

        autoYBoundsCheckbox.setSelected(value);
        App.graphConfig.setAutoYBounds(value);
        App.graphController.update();

    }

    public void resetBounds() {

        forceAutoYBounds(false);
        App.graphController.setBounds(App.graphConfig.getXMin(), App.graphConfig.getXMax(), App.graphConfig.getYMin(), App.graphConfig.getYMax());

    }

}
