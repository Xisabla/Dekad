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

public class Function extends VBox {

    private final transient DekadApp app;

    private transient int functionId;

    private transient MathFunction mathFunction;

    private transient boolean show;

    private static int counter = 1;

    // TODO: Add color management

    @FXML
    private transient Text functionName;

    @FXML
    private transient TextField functionExpression;

    @FXML
    private transient Button showHideButton;

    public Function(final DekadApp app, final MathFunction mathFunction) {

        super();

        this.app = app;
        this.show = true;
        this.mathFunction = mathFunction;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/function.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            this.functionId = counter++;

            functionName.setText(String.format("f%d(%s) = ", functionId, mathFunction.getArg()));
            functionExpression.setText(mathFunction.getExpression().getExpressionString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public MathFunction getMathFunction() {
        return mathFunction;
    }

    public boolean doesShow() {
        return show;
    }

    public void update() {
        final MathFunction mathFunction = new MathFunction(functionExpression.getText());

        if(mathFunction.isValid()) {
            functionExpression.setStyle("");

            this.mathFunction = mathFunction;
            functionName.setText(String.format("f%d(%s) = ", functionId, mathFunction.getArg()));

            app.getFunctionsPane().update();
        } else {
            functionExpression.setStyle("-fx-border-color: red");
        }
    }

    public void showHide() {
        show = !show;
        showHideButton.setText(show ? "Hide" : "Show");
        app.getFunctionsPane().update();
    }

    public void derivate() {
        // TODO: Derivate
        // Option 1: Create a derivate instruction to parse e.g: "deriv(f1)", "5*deriv(f1)+2", ...
        // Option 2: Use a boolean flag "derivate", if on true, plot derivate of the function while plotting the function
    }

    public void remove() {
        app.getFunctionsPane().remove(this);
    }

}
