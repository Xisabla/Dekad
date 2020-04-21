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

    private DekadApp app;

    @FXML
    private VBox functionsList;

    @FXML
    private TextField functionField;

    public FunctionsPane(DekadApp app) {

        this.app = app;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/functionspane.fxml"));

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
            Function function = new Function(app, mathFunction);
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

        for(Node child : functionsList.getChildren()) {
            if(child instanceof Function) {
                Function function = (Function) child;

                app.getGraph().addFunctions(function);
            }
        }

        app.getGraph().update();

    }

}
