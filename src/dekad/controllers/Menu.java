package dekad.controllers;

import dekad.core.DekadApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Menu extends AnchorPane {

    /**
     * All controllers a containing the instance of the DekadApp,
     *  it allows them to access to the other controllers and the settings
     *  without using static members
     */
    private final transient DekadApp app;

    @FXML
    private transient CheckMenuItem computedBoundsCheckbox;

    public Menu(final DekadApp app) {

        super();

        this.app = app;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/menu.fxml"));

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

        // TODO: [Priority:2]
        //  - On enter computedBounds: store old bounds
        //  - On leave: restore old  bounds
        app.settings().setPlotBoundsComputed(computedBoundsCheckbox.isSelected());
        app.getGraph().update();

    }

    public void setComputedBounds(final boolean value) {

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
