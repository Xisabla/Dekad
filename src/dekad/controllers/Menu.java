package dekad.controllers;

import dekad.core.DekadApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Menu extends AnchorPane {

    private DekadApp app;

    @FXML
    private CheckMenuItem computedBoundsCheckbox;

    public Menu(DekadApp app) {

        this.app = app;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/menu.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            app.setMenu(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void exit() {
        System.exit(0);
    }

    public void toggleComputedBounds() {

        // TODO:
        //  - On enter computedBounds: store old bounds
        //  - On leave: restore old  bounds
        app.settings().setPlotBoundsComputed(computedBoundsCheckbox.isSelected());
        app.getGraph().update();

    }

    public void setComputedBounds(boolean value) {

        computedBoundsCheckbox.setSelected(value);
        app.settings().setPlotBoundsComputed(value);
        app.getGraph().update();

    }

    public void resetBounds() {

        setComputedBounds(false);
        app.getGraph().setBounds(
                app.settings().getPlotXMin(),
                app.settings().getPlotXMax(),
                app.settings().getPlotYMin(),
                app.settings().getPlotYMax()
        );

    }

}
