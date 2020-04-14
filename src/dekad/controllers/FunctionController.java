package dekad.controllers;

import dekad.core.App;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class FunctionController implements Initializable {

    @FXML
    public transient Text invalidText;

    @FXML
    public transient TextField functionField;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        System.out.println("Function Controller loaded.");

    }

    public void append() {

        // Create the new function
        final MathFunction mathFunction = new MathFunction(functionField.getText());

        // TODO: Replace this behavior with an error popup
        invalidText.setVisible(false);

        // Check if the expression is valid
        if (mathFunction.isValid()) {
            // Add it to the graph and plot it
            App.graphController.addFunction(mathFunction);
            App.graphController.update();

        } else {
            // Tell the user that there is something wrong
            invalidText.setVisible(true);

        }

    }

}
