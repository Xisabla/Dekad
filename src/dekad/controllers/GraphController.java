package dekad.controllers;

import dekad.core.Graph;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    public static GraphController graphController;

    private List<MathFunction> functions;

    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    @FXML
    private LineChart<Double, Double> chart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private Graph graph;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        graphController = this;

        graph = Graph.getInstance(chart);
        functions = new ArrayList<>();

        xMin = -10;
        xMax = 10;
        yMin = -10;
        yMax = 10;

        updateBounds();

        // Sample function
        functions.add(new MathFunction("sinc(x)"));

        update();

        System.out.println("Graph Controller loaded.");

    }

    public void addFunction(MathFunction function) {

        functions.add(function);

    }

    public void updateBounds() {

        xAxis.setLowerBound(xMin);
        xAxis.setUpperBound(xMax);
        yAxis.setLowerBound(yMin);
        yAxis.setUpperBound(yMax);

    }

    public void updateFunctions() {

        for (MathFunction function : functions) {
            graph.plot(function, xMin, xMax);
        }

    }

    public void update() {

        graph.clear();
        updateBounds();
        updateFunctions();

    }
}
