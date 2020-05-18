package dekad.controllers;

import dekad.core.DekadApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        doAppend(functionField.getText());

    }

    public void doAppend(String expression) {

        Function function = new Function(app, expression);
        functionsList.getChildren().add(function);

        updateFunctionsMXFunctions();

        if(function.getMathFunction().isValid()) {

            update();

        } else {

            remove(function);

            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Invalid expression");
            alert.setHeaderText("The expression you entered seems to be invalid.");
            alert.setContentText("Error details:\n" + function.getMathFunction().getFunction().getErrorMessage());

            alert.show();

        }

    }

    public void remove(final Node node) {
        functionsList.getChildren().removeAll(node);
        update();
    }

    public List<Function> getFunctions() {

        List<Function> functions = new ArrayList<>();

        for(final Node child : functionsList.getChildren()) {
            if(child instanceof Function) {
                final Function function = (Function) child;

                functions.add(function);
            }
        }

        return functions;

    }

    public void update() {
        app.getGraph().clear();
        app.getFunctionsPane().updateFunctionsMXFunctions();

        for(final Function function : getFunctions()) {

            app.getGraph().addFunctions(function);
        }

        app.getGraph().update();

    }

    public void updateFunctionsMXFunctions() {

        for(final Function function : getFunctions()) {

            function.getMathFunction().getFunction().removeAllFunctions();

            List<org.mariuszgromada.math.mxparser.Function> mXFunctions = new ArrayList<>();

            for(final Function otherFunction : getFunctions()) {

                if(!otherFunction.getMathFunction().getName().equals(function.getMathFunction().getName())) {

                    function.getMathFunction().getFunction().addFunctions(otherFunction.getMathFunction().getFunction());

                }

            }


        }

    }

}
