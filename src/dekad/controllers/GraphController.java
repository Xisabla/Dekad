package dekad.controllers;

import dekad.core.Graph;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import java.net.URL;
import java.util.ResourceBundle;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

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

        graph.plot(x -> {
            if(x == 0) return (double) 1;
            return sin(PI*x)/(PI*x);
        }, -10, 10);

        xAxis.setLowerBound(-10);
        xAxis.setUpperBound(10);
        yAxis.setLowerBound(-0.5);
        yAxis.setUpperBound(1.1);

    }
}
