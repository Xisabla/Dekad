package dekad.controllers;

import dekad.core.Graph;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.net.URL;
import java.util.ResourceBundle;

public class FunctionsController implements Initializable {

    @FXML
    public TextField function;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(location);
    }

    public void draw() {

        Argument x = new Argument("x");
        Expression e = new Expression(function.getText(), x);

        Graph graph = Graph.getInstance();
        graph.clear();
        graph.plot(e, -10, 10);

        /*
        TODO: Use this to tell to the user when the syntax is bad:
        mXparser.consolePrint(e.checkSyntax());
        mXparser.consolePrint(e.checkLexSyntax());
        mXparser.consolePrint(e.getExpressionString());
        */

    }

}
