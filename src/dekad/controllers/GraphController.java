package dekad.controllers;

import dekad.core.Graph;
import dekad.core.App;
import dekad.models.GraphConfig;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    private final GraphConfig graphConfig = App.graphConfig;

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
    public void initialize(final URL location, final ResourceBundle resources) {

        graphController = this;
        App.updateControllers();

        graph = Graph.getInstance(chart);
        functions = new ArrayList<>();

        graph.setOffset(graphConfig.getOffset());

        xMin = graphConfig.getXMin();
        xMax = graphConfig.getXMax();
        yMin = graphConfig.getYMin();
        yMax = graphConfig.getYMax();

        updateBounds();

        // Sample function
        functions.add(new MathFunction("sinc(x)"));
        update();

        System.out.println("Graph Controller loaded.");

    }

    public void addFunction(final MathFunction function) {

        functions.add(function);

    }

    public void updateBounds() {

        if (graphConfig.hasAutoYBounds()) {
            processYBounds();
        } else {
            yMin = graphConfig.getYMin();
            yMax = graphConfig.getYMax();
        }

        xAxis.setLowerBound(xMin);
        xAxis.setUpperBound(xMax);
        yAxis.setLowerBound(yMin);
        yAxis.setUpperBound(yMax);

    }

    public void updateFunctions() {

        for (final MathFunction function : functions) {
            graph.plot(function, xMin, xMax);
        }

    }

    public void update() {

        graph.clear();
        updateBounds();
        updateFunctions();

    }

    public void processYBounds() {

        if (functions.isEmpty()) {

            yMin = graphConfig.getYMin();
            yMax = graphConfig.getYMax();

        } else {

            final List<Double> max = new ArrayList<>();
            final List<Double> min = new ArrayList<>();

            for (int i = 0; i < functions.size(); i++) {

                max.add(i, Double.MIN_VALUE);
                min.add(i, Double.MAX_VALUE);

            }

            for (int i = 0; i < functions.size(); i++) {

                for (double x = xMin; x <= xMax; x += graph.getOffset()) {

                    final double y = functions.get(i).eval(x);

                    if (y > max.get(i)) max.set(i, y);
                    if (y < min.get(i)) min.set(i, y);

                }

            }

            yMax = Collections.min(max) * graphConfig.getAutoYBoundsFactor();
            yMin = Collections.max(min) * graphConfig.getAutoYBoundsFactor();

        }

    }

}
