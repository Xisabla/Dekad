package dekad.controllers;

import dekad.core.App;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class FunctionsPaneController implements Initializable {

    @FXML
    public VBox functionsList;

    @FXML
    public transient TextField functionField;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        System.out.println("Function Controller loaded.");

    }

    public void append() {

        final MathFunction mathFunction = new MathFunction(functionField.getText());

        if(mathFunction.isValid()) {

            Function function = new Function(this, mathFunction);

            functionsList.getChildren().add(function);

            updateFunctions();

        }

    }

    public void remove(final Node node) {

        functionsList.getChildren().removeAll(node);

        updateFunctions();

    }

    public void updateFunctions() {

        App.graphController.clearFunctions();

        for(Node elem : functionsList.getChildren()) {

            if(elem instanceof Function) {

                Function function = (Function) elem;

                if(function.doesShow()) App.graphController.addFunction(function.getMathFunction());

            }

        }

        App.graphController.update();

    }

}
