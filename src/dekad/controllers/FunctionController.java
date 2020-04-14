package dekad.controllers;

import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class FunctionController implements Initializable {

    private GraphController graphController;

    @FXML
    public Text invalidText;

    @FXML
    public TextField functionField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println("Function Controller loaded.");

    }

    public void append() {

        if (graphController == null) graphController = GraphController.graphController;

        MathFunction mathFunction = new MathFunction(functionField.getText());

        invalidText.setVisible(false);

        if (mathFunction.isValid()) {

            graphController.addFunction(mathFunction);
            graphController.update();

        } else {

            invalidText.setVisible(true);

        }

    }

}
