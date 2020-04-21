package dekad.controllers;

import dekad.core.DekadApp;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class FunctionsPane extends VBox {

    private final transient DekadApp app;

    @FXML
    private transient VBox functionsList;

    @FXML
    private transient TextField functionField;

    public FunctionsPane(final DekadApp app) {

        super();

        this.app = app;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/functionspane.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            app.setFunctionsPane(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void append() {

        final MathFunction mathFunction = new MathFunction(functionField.getText());

        if(mathFunction.isValid()) {
            final Function function = new Function(app, mathFunction);
            functionsList.getChildren().add(function);
            update();
        } /*else {
            // TODO: Open a window, to tell a user there's something wrong.
        }*/

    }

    public void remove(final Node node) {
        functionsList.getChildren().removeAll(node);
        update();
    }

    public void update() {
        app.getGraph().clear();

        for(final Node child : functionsList.getChildren()) {
            if(child instanceof Function) {
                final Function function = (Function) child;

                app.getGraph().addFunctions(function);
            }
        }

        app.getGraph().update();

    }

}
