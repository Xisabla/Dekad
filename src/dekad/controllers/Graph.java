package dekad.controllers;

import dekad.core.DekadApp;
import dekad.models.GraphManager;
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

    /**
     * All controllers a containing the instance of the DekadApp,
     *  it allows them to access to the other controllers and the settings
     *  without using static members
     */
    private final transient DekadApp app;

    /**
     * Functions list of the graph, they will all be plotted if they don't have to show tag on false
     */
    private transient List<Function> functions;

    /**
     * Graph bounds data, will be updated on bounds computing or on drag/zoom
     */
    private transient double xMin;
    private transient double xMax;
    private transient double yMin;
    private transient double yMax;

    /**
     * The GraphManager object that will plot the functions of the function list
     */
    private transient GraphManager graphManager;

    /**
     * Drag behavior
     * Stores the old position of the cursor on dragging to compute graph dragging
     */
    private transient double lastDragX = NaN;
    private transient double lastDragY = NaN;

    /**
     * JavaFX Chart on which the functions will be plotted
     */
    @FXML
    private transient LineChart<Double, Double> chart;

    /**
     * JavaFX xAxis NumberAxis
     */
    @FXML
    private transient NumberAxis xAxis;

    /**
     * JavaFX yAxis NumberAxis
     */
    @FXML
    private transient NumberAxis yAxis;

    /**
     * Instantiate the Graph and initialize it's values and behavior
     * @param app
     */
    public Graph(final DekadApp app) {

        super();

        this.app = app;

        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dekad/views/graph.fxml"));

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

    /**
     * Add a function to the function list
     * @param function The Function to add
     */
    public void addFunctions(final Function function) {
        functions.add(function);
    }

    /**
     * Remove all the functions from the list and clear the graph
     */
    public void clear() {
        functions.clear();
        graphManager.clear();
    }

    /**
     * Never used, should be for overriding the function list/reset it
     * @param functions New function list
     */
    public void setFunctions(final List<Function> functions) {
        this.functions = functions;
    }

    /**
     * Sets the bounds of the axis of the graph with the given values
     * @param xMin X lower bound
     * @param xMax X upper bound
     * @param yMin Y lower bound
     * @param yMax Y upper bound
     */
    public void setBounds(final double xMin, final double xMax, final double yMin, final double yMax) {

        // Set the attributes
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;

        // Update change the bounds and updates the plot
        update();

    }

    /**
     * Default update, execute the main update with computingOffset as true
     */
    public final void update() {
        update(true);
    }

    /**
     * Clears the graph and update the plot with functions in the list (also binds the bounds)
     * @param doesComputeOffset if set on true, the graph will compute and set automatically the Y bounds
     */
    public void update(final boolean doesComputeOffset) {

        graphManager.clear();
        bindBounds();

        if(doesComputeOffset) computeOffset();

        plot();

    }

    /**
     * Set the binds of the xAxis and yAxis relatively to the values in attributes,
     *  computes Y bounds if set on true in the settings
     */
    public void bindBounds() {

        if(app.settings().isPlotBoundsComputed()) {
            computeBounds();
        }

        xAxis.setLowerBound(xMin);
        xAxis.setUpperBound(xMax);
        yAxis.setLowerBound(yMin);
        yAxis.setUpperBound(yMax);

    }

    /**
     * Plot all the functions on the graph through the GraphManager
     */
    public void plot() {

        for (final Function function : functions) {
            if(function.doesShow()) graphManager.plot(function, xMin, xMax);
        }

    }

    /**
     * Compute and updates yMin and yMax relatively to the functions minimum values
     */
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

    /**
     * Compute the offset between each point the have the same performance while being zoom or unzoomed
     */
    private void computeOffset() {

        // Compute ratio
        final double baseWidth = app.settings().getPlotXMax() - app.settings().getPlotXMin();
        final double currentWidth = xMax - xMin;
        final double ratio = currentWidth / baseWidth;

        // Update offset
        graphManager.setOffset(app.settings().getPlotOffsetDefault() * ratio);

    }

    /**
     * Handle mouse dragging on the graph, allows the graph to "move" on dragging
     * @param event MouseEvent relative to the drag
     */
    private void handleChartMouseDragged(final MouseEvent event) {

        if (isNaN(lastDragX) || isNaN(lastDragY)) {

            // Automatically disable Auto Y Bounds
            app.getMenu().setComputedBounds(false);

        } else {

            // Remove the axis width (~= 0.05% of the total chart width)
            final double chartWidth = chart.getWidth() * 0.95;
            final double chartHeight = chart.getHeight() * 0.95;
            final double graphWidth = xMax - xMin;
            final double graphHeight = yMax - yMin;
            // Compute ratio between the Graph Bounds and the Chart Width
            final double widthRatio = graphWidth / chartWidth;
            final double heightRatio = graphHeight / chartHeight;

            // Compute the difference with the last event
            final double chartDiffX = lastDragX - event.getX();
            final double chartDiffY = event.getY() - lastDragY;

            // Compute the Graph Bounds changes
            final double graphDiffX = chartDiffX * widthRatio;
            final double graphDiffY = chartDiffY * heightRatio;

            // Lower the precision during the process to avoid lags
            final double baseOffset = graphManager.getOffset();
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

        }

        lastDragX = event.getX();
        lastDragY = event.getY();

    }

    /**
     * Resets the variables on drag release
     * @param event Mouse Event on drag release
     */
    private void handleChartMouseRelease(final MouseEvent event) {

        // Reset lastDrag variables
        lastDragX = NaN;
        lastDragY = NaN;
        update(true);

    }

    /**
     * Handle the mouse scrolling on the graph, allows to zoom or unzoom the graph
     * @param event Mouse ScrolleEvent
     */
    private void handleChartScroll(final ScrollEvent event) {

        app.getMenu().setComputedBounds(false);

        double zoomRatio = app.settings().getPlotBoundsZoomRatio();

        if (event.getDeltaY() > 0) {
            // Zoom in
            xMin *= zoomRatio;
            xMax *= zoomRatio;
            yMin *= zoomRatio;
            yMax *= zoomRatio;

        } else {
            // Zoom out
            xMin /= zoomRatio;
            xMax /= zoomRatio;
            yMin /= zoomRatio;
            yMax /= zoomRatio;

        }

        // Update computing the offset
        update(true);

    }

}
