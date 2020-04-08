package dekad.controllers;

import dekad.core.Graph;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.net.URL;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    @FXML
    private LineChart<Double, Double> chart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private Graph graph;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        graph = new Graph(chart);

        Argument x = new Argument("x");
        Expression e = new Expression("sinc(x)", x);

        graph.plot(e, -10, 10);

        xAxis.setLowerBound(-10);
        xAxis.setUpperBound(10);
        yAxis.setLowerBound(-1.1);
        yAxis.setUpperBound(1.1);

    }
}
