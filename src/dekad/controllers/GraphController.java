package dekad.controllers;

import dekad.core.Graph;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    public static GraphController graphController;

    private List<MathFunction> functions;

    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;

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

        xmin = -10;
        xmax = 10;
        ymin = -10;
        ymax = 10;

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

        xAxis.setLowerBound(xmin);
        xAxis.setUpperBound(xmax);
        yAxis.setLowerBound(ymin);
        yAxis.setUpperBound(ymax);

    }

    public void updateFunctions() {

        for (MathFunction function : functions) {
            graph.plot(function, xmin, xmax);
        }

    }

    public void update() {

        graph.clear();
        updateBounds();
        updateFunctions();

    }
}
