package dekad.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class OldFunctionController implements Initializable {

    public static int idCnt = 1;

    private int id;

    @FXML
    public Text functionName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.id = idCnt++;

        functionName.setText(String.format("f%d(%s)", this.id, "x"));

        System.out.println("FunctionController loaded.");

    }

}
