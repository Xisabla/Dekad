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
    public CheckMenuItem computedYBoundsCheckbox;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        // Set Controller available
        menuController = this;
        App.updateControllers();

        // Initialize computed offset status
        computedYBoundsCheckbox.setSelected(App.graphConfig.hasComputedYBounds());

        System.out.println("Menu Controller loaded.");

    }

    public void exit() {
        System.exit(0);
    }

    public void toggleComputedYBounds() {

        App.graphConfig.setComputedYBounds(computedYBoundsCheckbox.isSelected());
        App.graphController.update();

    }

    public void forceComputedYBounds(boolean value) {

        computedYBoundsCheckbox.setSelected(value);
        App.graphConfig.setComputedYBounds(value);
        App.graphController.update();

    }

    public void resetBounds() {

        forceComputedYBounds(false);
        App.graphController.setBounds(App.graphConfig.getXMin(), App.graphConfig.getXMax(), App.graphConfig.getYMin(), App.graphConfig.getYMax());

    }

}
