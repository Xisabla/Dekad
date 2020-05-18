package dekad.controllers;

import dekad.core.DekadApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Menu Object that allow the other to changes preferences (updates Settings) or exit
 * (more features coming one day?)
 */
public class Menu extends AnchorPane {

    /**
     * All controllers a containing the instance of the DekadApp,
     *  it allows them to access to the other controllers and the settings
     *  without using static members
     */
    private final transient DekadApp app;

    /**
     * JavaFX Checkbox to know if the Y bounds should auto compute
     */
    @FXML
    private transient CheckMenuItem computedBoundsCheckbox;

    /**
     * Instantiate the Menu
     * @param app DekadApp main instance
     */
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

    /**
     * JavaFX trigger, quit the application
     */
    public void exit() {
        System.exit(0);
    }

    /**
     * JavaFX trigger
     * Toggle the computed bounds value relatively the status of the checkbox and then updates the graph
     */
    public void toggleComputedBounds() {

        // TODO: [Priority:2]
        //  - On enter computedBounds: store old bounds
        //  - On leave: restore old  bounds
        app.settings().setPlotBoundsComputed(computedBoundsCheckbox.isSelected());
        app.getGraph().update();

    }

    /**
     * Force the Y computed bounds status in settings, update checkbox and then update the graph
     * @param value New value of the Y computed bounds status
     */
    public void setComputedBounds(final boolean value) {

        computedBoundsCheckbox.setSelected(value);
        app.settings().setPlotBoundsComputed(value);
        app.getGraph().update();

    }

    /**
     * Reset the bounds to the default one from the settings and then update the graph
     */
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
