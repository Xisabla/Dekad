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


    private DekadApp app;

    private int functionId;

    private MathFunction mathFunction;

    private boolean show;

    private static int counter = 1;

    // TODO: Add color management

    @FXML
    private Text functionName;

    @FXML
    private TextField functionExpression;

    @FXML
    private Button showHideButton;

    public Function(DekadApp app, MathFunction mathFunction) {

        this.app = app;
        this.show = true;
        this.mathFunction = mathFunction;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/function.fxml"));

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
        MathFunction mathFunction = new MathFunction(functionExpression.getText());

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

    public void changeParameter() {
        // TODO: Open a window to set the parameter
    }

    public void remove() {
        app.getFunctionsPane().remove(this);
    }

}
