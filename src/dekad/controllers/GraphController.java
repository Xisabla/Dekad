package dekad.controllers;

import dekad.core.Graph;
import dekad.core.App;
import dekad.models.GraphConfig;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Double.*;

public class GraphController implements Initializable {

    private final GraphConfig graphConfig = App.graphConfig;

    public static GraphController graphController;

    private List<MathFunction> functions;

    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;

    private Graph graph;

    private double lastDragX = NaN;
    private double lastDragY = NaN;

    @FXML
    private LineChart<Double, Double> chart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

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

        chart.setOnMouseDragged(this::handleChartMouseDragged);
        chart.setOnMouseReleased(this::handleChartMouseRelease);

        System.out.println("Graph Controller loaded.");

    }

    public void addFunction(final MathFunction function) {

        functions.add(function);

    }

    public void updateBounds() {

        if (graphConfig.hasAutoYBounds()) {
            computeYBounds();
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

    public void setBounds(double xMin, double xMax, double yMin, double yMax) {

        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

        update();

    }

    private void computeYBounds() {

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

    private void handleChartMouseDragged(MouseEvent event) {

        if(!isNaN(lastDragX)) {

            // Remove the axis width (~= 0.05% of the total chart width)
            double chartWidth = chart.getWidth() * 0.95;
            double chartHeight = chart.getHeight() * 0.95;
            double graphWidth = xMax - xMin;
            double graphHeight = yMax - yMin;
            // Compute ratio between the Graph Bounds and the Chart Width
            double widthRatio = graphWidth / chartWidth;
            double heightRatio = graphHeight / chartHeight;

            // Compute the difference with the last event
            double chartDiffX = (lastDragX - event.getX());
            double chartDiffY = (event.getY() - lastDragY);

            // Compute the Graph Bounds changes
            double graphDiffX = chartDiffX * widthRatio;
            double graphDiffY = chartDiffY * heightRatio;

            // Lower the precision during the process to avoid lags
            // TODO: Calculate offset depending on the Graph Width
            // TODO: Then change computingOffset to computingOffsetRatio to allow
            //  the user to get more points while computing changes
            graph.setOffset(graphConfig.getComputingOffset());

            xMin += graphDiffX;
            xMax += graphDiffX;
            yMin += graphDiffY;
            yMax += graphDiffY;

            update();

            // Reset the precision
            graph.setOffset(graphConfig.getOffset());

            System.out.println(event.getY());

        } else {

            // Automatically disable Auto Y Bounds
            App.menuController.forceAutoYBounds(false);

        }

        lastDragX = event.getX();
        lastDragY = event.getY();

    }

    private void handleChartMouseRelease(MouseEvent event) {

        lastDragX = NaN;
        update();

    }

}
