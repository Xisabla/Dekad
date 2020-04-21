package dekad.controllers;

import dekad.core.DekadApp;
import dekad.models.GraphManager;
import dekad.models.MathFunction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;

public class Graph extends VBox {

    private DekadApp app;

    /**
     * Functions list of the graph
     */
    private List<Function> functions;

    /**
     * Graph main data
     */
    private double xMin;
    private double xMax;
    private double yMin;
    private double yMax;
    private GraphManager graphManager;

    /**
     * Drag behavior
     */
    private double lastDragX = NaN;
    private double lastDragY = NaN;

    @FXML
    private LineChart<Double, Double> chart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    public Graph(DekadApp app) {

        this.app = app;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/graph.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();

            app.setGraph(this);

            // Initialize graph
            graphManager = new GraphManager(chart);
            graphManager.setOffset(app.settings().getPlotOffsetDefault());

            // Initialize main data
            functions = new ArrayList<>();
            xMin = app.settings().getPlotXMin();
            xMax = app.settings().getPlotXMax();
            yMin = app.settings().getPlotYMin();
            yMax = app.settings().getPlotYMax();

            update();

            // Set event handlers
            chart.setOnMouseDragged(this::handleChartMouseDragged);
            chart.setOnMouseReleased(this::handleChartMouseRelease);
            chart.setOnScroll(this::handleChartScroll);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void addFunctions(Function function) {
        functions.add(function);
    }

    public void clear() {
        functions.clear();
        graphManager.clear();
    }

    public void setFunctions(List<Function> functions) {
        this.functions = functions;
    }

    public void setBounds(double xMin, double xMax, double yMin, double yMax) {

        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

        update();

    }

    public void update() {
        update(true);
    }

    public void update(boolean doesComputeOffset) {

        graphManager.clear();
        bindBounds();

        if(doesComputeOffset) computeOffset();

        plot();

    }

    public void bindBounds() {

        if(app.settings().isPlotBoundsComputed()) {
            computeBounds();
        }

        xAxis.setLowerBound(xMin);
        xAxis.setUpperBound(xMax);
        yAxis.setLowerBound(yMin);
        yAxis.setUpperBound(yMax);

    }

    public void plot() {

        for (final Function function : functions) {
            if(function.doesShow()) graphManager.plot(function, xMin, xMax);
        }

    }

    private void computeBounds() {

        // Just reset if there is not functions
        if (functions.isEmpty()) {

            yMin = app.settings().getPlotYMin();
            yMax = app.settings().getPlotYMax();

        } else {

            final List<Double> max = new ArrayList<>();
            final List<Double> min = new ArrayList<>();

            for (int i = 0; i < functions.size(); i++) {

                max.add(i, Double.MIN_VALUE);
                min.add(i, Double.MAX_VALUE);

            }

            // Get the min and the max of each functions
            for (int i = 0; i < functions.size(); i++) {

                for (double x = xMin; x <= xMax; x += graphManager.getOffset()) {

                    final double y = functions.get(i).getMathFunction().eval(x);

                    if (y > max.get(i)) max.set(i, y);
                    if (y < min.get(i)) min.set(i, y);

                }

            }

            // Choose to focus and the "smallest" function
            yMax = Collections.min(max) * app.settings().getPlotBoundsComputefactor();
            yMin = Collections.max(min) * app.settings().getPlotBoundsComputefactor();

        }

    }

    private void computeOffset() {

        double offset = graphManager.getOffset();

        // Compute ratio
        double baseWidth = app.settings().getPlotXMax() - app.settings().getPlotXMin();
        double currentWidth = xMax - xMin;
        double ratio = currentWidth / baseWidth;

        // Update offset
        graphManager.setOffset(app.settings().getPlotOffsetDefault() * ratio);

    }

    private void handleChartMouseDragged(MouseEvent event) {

        if (!isNaN(lastDragX) && !isNaN(lastDragY)) {
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
            double baseOffset = graphManager.getOffset();
            graphManager.setOffset(app.settings().getPlotOffsetComputing());

            // Move the bounds
            xMin += graphDiffX;
            xMax += graphDiffX;
            yMin += graphDiffY;
            yMax += graphDiffY;

            // Update, don't compute offset it will be done on mouse drag release
            update(false);

            // Reset the base offset
            graphManager.setOffset(baseOffset);

        } else {

            // Automatically disable Auto Y Bounds
            app.getMenu().setComputedBounds(false);

        }

        lastDragX = event.getX();
        lastDragY = event.getY();

    }

    private void handleChartMouseRelease(MouseEvent event) {

        // Reset lastDrag variables
        lastDragX = NaN;
        lastDragY = NaN;
        update(true);

    }

    private void handleChartScroll(ScrollEvent event) {

        app.getMenu().setComputedBounds(false);

        // TODO: Replace 0.7 by settings().getPlotBoundsZoomratio();
        if (event.getDeltaY() > 0) {
            // Zoom in
            xMin *= 0.7;
            xMax *= 0.7;
            yMin *= 0.7;
            yMax *= 0.7;

        } else {
            // Zoom out
            xMin /= 0.7;
            xMax /= 0.7;
            yMin /= 0.7;
            yMax /= 0.7;

        }

        // Update computing the offset
        update(true);

    }

}
