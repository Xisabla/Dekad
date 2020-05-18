package dekad.models;

import dekad.controllers.Function;
import javafx.scene.chart.XYChart;

/**
 * Graph Manager that plots functions on the given Graph
 */
public class GraphManager {

    /**
     * JavaFX Chart in which the functions will be plotted
     */
    private final transient XYChart<Double, Double> chart;

    /**
     * Offset (on X axis) between 2 points
     */
    private double offset;

    /**
     * Initialize a Graph Manager
     * @param chart The chart in which the functions will be plotted
     */
    public GraphManager(final XYChart<Double, Double> chart) {
        this.chart = chart;
        this.offset = 0.01;
    }

    /**
     * Get the current offset
     * @return The offset
     */
    public double getOffset() {
        return offset;
    }

    /**
     * Set the offset
     * @param offset Offset between two points on X axis
     */
    public void setOffset(final double offset) {
        this.offset = offset;
    }

    /**
     * Plot the given Function between the given bounds
     * @param function Function to plot
     * @param min Xmin bound
     * @param max Xmax bound
     */
    public void plot(final Function function, final double min, final double max) {

        final XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (double x = min; x <= max; x += offset) {

            plotPoint(x, function.getMathFunction().eval(x), series);

        }

        chart.getData().add(series);

    }

    /**
     * Append a point the series (one series = one function)
     * @param x X position of the point
     * @param y Y position of the point
     * @param series Relative series to the function
     */
    private void plotPoint(final double x, final double y, final XYChart.Series<Double, Double> series) {

        series.getData().add(new XYChart.Data<>(x, y));

    }

    /**
     * Remove all the points from the graph (clear the series)
     */
    public void clear() {

        chart.getData().clear();

    }
}
