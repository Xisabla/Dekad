package dekad.controllers;

import dekad.core.App;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class Function extends VBox {

    public static int fncCnt = 0;

    private final int id;

    private final FunctionsPaneController parentController;

    private MathFunction mathFunction;

    private boolean show;

    @FXML
    private Text functionName;

    @FXML
    private TextField functionExpression;

    @FXML
    private Button showHideButton;

    // TODO: Add a color field and a Button to edit the color

    public Function(FunctionsPaneController parentController, MathFunction mathFunction) {

        this.parentController = parentController;
        this.mathFunction = mathFunction;
        this.show = true;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/function.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        // Try to load the Function fxml element
        try {
            fxmlLoader.load();

            id = ++fncCnt;

            // Set it's name it's function's text
            functionName.setText(String.format("f%d(%s) = ", id, mathFunction.getArg()));
            functionExpression.setText(mathFunction.getExpression().getExpressionString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int getFId() {

        return id;

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

            parentController.updateFunctions();
        } else {
            functionExpression.setStyle("-fx-border-color: red");
        }

    }

    public void showHide() {


        show = !show;

        showHideButton.setText(show ? "Hide" : "Show");

        parentController.updateFunctions();

    }

    public void changeParameter() {
        // TODO: Do (open a popup to change the parameter and also the expression)
    }

    public void remove() {

        parentController.remove(this);

    }

}
