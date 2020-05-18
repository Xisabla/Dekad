package dekad.controllers;

import dekad.core.DekadApp;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Function Controller
 * Each functions inside the FunctionsPane is bind to one of this controllers,
 *  they should all be children of the FunctionsPane and contains the MathFunction
 *  related to the given expression
 */
public class Function extends VBox {

    /**
     * Function Counter to set the functionId
     */
    private static int counter = 1;

    /**
     * All controllers a containing the instance of the DekadApp,
     *  it allows them to access to the other controllers and the settings
     *  without using static members
     */
    private final transient DekadApp app;

    /**
     * Custom id which is not the Node id but a simple number to differentiate all the functions on the FunctionsPane
     */
    private final transient int functionId;

    /**
     * MathFunction related to the function's expression, used to compute points (and derivative) as well as
     * to detect the argument
     */
    private transient MathFunction mathFunction;

    /**
     * If set on false, the function won't be plotted on the next GraphManager update
     */
    private transient boolean show;

    /**
     * TODO
     * Will allow to use custom color on while plotting on the Graph
     */
    // TODO: [Priority:3] Add color management
    // private transient Color? color

    /**
     * JavaFX element of the Text showing the function name,
     *  used to update the function name/argument
     */
    @FXML
    private transient Text functionName;

    /**
     * JavaFX element of the TextField which contains the function expression
     */
    @FXML
    private transient TextField functionExpression;

    /**
     * JavaFX element of the Button that toggle the visibility of the plot
     */
    @FXML
    private transient Button showHideButton;

    /**
     * Instantiate a new Function with the given expression
     * @param app The main DekadApp object
     * @param expression The string expression of the Function
     */
    public Function(final DekadApp app, final String expression) {

        super();

        this.app = app;
        // Default visibility is on true
        this.show = true;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/function.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        // Increment the id counter
        this.functionId = counter++;
        // Instantiate the relative MathFunction
        this.mathFunction = new MathFunction(app, expression, functionId);

        try {
            fxmlLoader.load();

            functionExpression.setText(mathFunction.getExpression());
            updateName();

            app.getFunctionsPane().updateFunctionsMXFunctions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Fetch the function expression and argument from the MathFunction object
     */
    private void updateName() {
        functionName.setText(String.format("%s(%s) = ", mathFunction.getName(), mathFunction.getArg()));
    }

    /**
     * JavaFX trigger
     * Updates the MathFunction object with the given expression on the TextField
     * If the MathFunction is not valid (wrong syntax), won't append plot changes and will set the TextField border
     *  color to red to notify the user
     */
    public void update() {

        final MathFunction mathFunction = new MathFunction(app, functionExpression.getText(), functionId);

        if(mathFunction.isValid()) {
            // Clears the style because the border color could be set to red
            functionExpression.setStyle("");

            this.mathFunction = mathFunction;
            updateName();

            app.getFunctionsPane().update();
        } else {
            functionExpression.setStyle("-fx-border-color: red");
        }

    }

    /**
     * JavaFX trigger
     * Toggle the showing status and update the plot
     */
    public void showHide() {
        show = !show;
        showHideButton.setText(show ? "Hide" : "Show");
        app.getFunctionsPane().update();
    }

    /**
     * JavaFX trigger
     * Append a new Function in the FunctionsPane (and also on the plot) which is the derivative of the current one
     */
    public void derivative() {

        final String derivativeExpression = String.format("der(%s, %s, %s)",
                functionExpression.getText(),
                mathFunction.getArg(),
                mathFunction.getArg());

        app.getFunctionsPane().append(derivativeExpression);

    }

    /**
     * JavaFX trigger
     * Remove the current Function from the FunctionsPane children and then update the plot
     */
    public void remove() {
        app.getFunctionsPane().remove(this);
    }

    /*************** Getters *****************/

    public MathFunction getMathFunction() {
        return mathFunction;
    }

    /**
     * @return True if the function will be shown on the next GraphManager plot, false otherwise
     */
    public boolean doesShow() {
        return show;
    }

}
