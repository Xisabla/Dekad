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

    private static int counter = 1;

    private final transient DekadApp app;

    private final transient int functionId;

    // TODO: Remove
    private transient MathFunction mathFunction;

    private transient boolean show;


    // TODO: Add color management

    @FXML
    private transient Text functionName;

    @FXML
    private transient TextField functionExpression;

    @FXML
    private transient Button showHideButton;

    public Function(final DekadApp app, final String expression) {

        super();

        this.app = app;
        this.show = true;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/function.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        this.functionId = counter++;
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

    public MathFunction getMathFunction() {
        return mathFunction;
    }

    public boolean doesShow() {
        return show;
    }

    public void update() {

        final MathFunction mathFunction = new MathFunction(app, functionExpression.getText(), functionId);

        if(mathFunction.isValid()) {
            functionExpression.setStyle("");

            this.mathFunction = mathFunction;
            updateName();

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

    public void derivative() {

        String derivativeExpression = String.format("der(%s, %s, %s)",
                functionExpression.getText(),
                mathFunction.getArg(),
                mathFunction.getArg());

        app.getFunctionsPane().doAppend(derivativeExpression);

    }

    public void remove() {
        app.getFunctionsPane().remove(this);
    }

    private void updateName() {
        functionName.setText(String.format("%s(%s) = ", mathFunction.getName(), mathFunction.getArg()));
    }

}
