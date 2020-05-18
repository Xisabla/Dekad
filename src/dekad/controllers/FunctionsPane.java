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

/**
 * Container of all the Functions and TextField/Button to append a new Function
 */
public class FunctionsPane extends VBox {

    /**
     * All controllers a containing the instance of the DekadApp,
     *  it allows them to access to the other controllers and the settings
     *  without using static members
     */
    private final transient DekadApp app;

    /**
     * Container of all the Functions
     */
    @FXML
    private transient VBox functionsList;

    /**
     * JavaFX TextField in which the new Function expression is written by the user
     */
    @FXML
    private transient TextField functionField;

    /**
     * Instantiate the FunctionsPane
     * @param app
     */
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

    /**
     * JavaFX trigger
     * Append a function to the function's list while reading it's expression from the TextField
     */
    public void addFunction() {

        append(functionField.getText());

    }

    /**
     * "Manually" append a function into the list from a given expression
     * @param expression The expression of the function
     */
    public void append(final String expression) {

        final Function function = new Function(app, expression);
        functionsList.getChildren().add(function);

        // Update the functions related functions before checking if it is valid
        // In the other order this may make some bugs (functions like "f1(x)" won't be recognized by the new function)
        updateFunctionsMXFunctions();

        if(function.getMathFunction().isValid()) {

            // If everything ok, update to plot it
            update();

        } else {

            // Otherwise, remove the function
            remove(function);

            // And tell the user there's something wrong
            final Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Invalid expression");
            alert.setHeaderText("The expression you entered seems to be invalid.");
            alert.setContentText("Error details:\n" + function.getMathFunction().getMxFunction().getErrorMessage());

            alert.show();

        }

    }

    /**
     * Remove a given Node (Function) from the function's list and then update the plot to remove it from the graph
     * @param node The Node/Function to remove
     */
    public void remove(final Node node) {
        functionsList.getChildren().removeAll(node);
        update();
    }

    /**
     * Find and return all of the nodes of the function's list that are functions
     * @return A list of Function
     */
    public List<Function> getFunctions() {

        final List<Function> functions = new ArrayList<>();

        for(final Node child : functionsList.getChildren()) {
            if(child instanceof Function) {
                final Function function = (Function) child;

                functions.add(function);
            }
        }

        return functions;

    }

    /**
     * Update the function's list and then the graph, in the order:
     * - Clear the graph
     * - Update all the mXfunctions to be recognized inside the functions
     * - Add functions to the graph
     * - Plot them
     */
    public void update() {
        app.getGraph().clear();
        app.getFunctionsPane().updateFunctionsMXFunctions();

        for(final Function function : getFunctions()) {

            app.getGraph().addFunctions(function);
        }

        app.getGraph().update();

    }

    /**
     * Update the list of functions inside all the mXparser functions related to a MathFunction (then a Dekad Function)
     * Each function inside the mXparser functions can then be recognized on plotting, that allows things such as:
     * "f9(x) = 9 * f3(x) + f2(1) * f5(x) ^ f6(7)"
     */
    public void updateFunctionsMXFunctions() {

        // For each functions
        for(final Function function : getFunctions()) {

            // Clear all the recognized functions inside the mXparser Function
            function.getMathFunction().getMxFunction().removeAllFunctions();

            // Find all the others
            for(final Function otherFunction : getFunctions()) {

                // (which means that they have not the same name)
                if(!otherFunction.getMathFunction().getName().equals(function.getMathFunction().getName())) {

                    // And then append them the recognized functions
                    function.getMathFunction().getMxFunction().addFunctions(otherFunction.getMathFunction().getMxFunction());

                }

            }


        }

    }

}
