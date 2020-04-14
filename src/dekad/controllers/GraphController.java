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
import javafx.scene.input.ScrollEvent;

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
        chart.setOnScroll(this::handleChartScroll);

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

        update(true);

    }

    public void update(boolean doesComputeOffset) {

        graph.clear();
        updateBounds();

        if (doesComputeOffset) computeOffset();

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

    private void computeOffset() {

        double offset = graph.getOffset();
        double baseWidth = graphConfig.getYMax() - graphConfig.getXMin();
        double currentWidth = xMax - xMin;
        double ratio = currentWidth / baseWidth;

        graph.setOffset(graphConfig.getOffset() * ratio);

        System.out.println(offset + " --> " + graph.getOffset());

    }

    private void handleChartMouseDragged(MouseEvent event) {

        if (!isNaN(lastDragX)) {

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
            double baseOffset = graph.getOffset();
            graph.setOffset(baseOffset * graphConfig.getComputingOffsetRatio());

            xMin += graphDiffX;
            xMax += graphDiffX;
            yMin += graphDiffY;
            yMax += graphDiffY;

            update(false);

            graph.setOffset(baseOffset);

        } else {

            // Automatically disable Auto Y Bounds
            App.menuController.forceAutoYBounds(false);

        }

        lastDragX = event.getX();
        lastDragY = event.getY();

    }

    private void handleChartMouseRelease(MouseEvent event) {

        lastDragX = NaN;
        update(true);

    }

    private void handleChartScroll(ScrollEvent event) {

        App.menuController.forceAutoYBounds(false);

        // TODO: Replace 0.7 by GraphConfig.getZoomRatio()
        if (event.getDeltaY() > 0) {

            xMin *= 0.7;
            xMax *= 0.7;
            yMin *= 0.7;
            yMax *= 0.7;

        } else {

            xMin /= 0.7;
            xMax /= 0.7;
            yMin /= 0.7;
            yMax /= 0.7;

        }

        update(true);

    }

}
