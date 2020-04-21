package dekad.models;

import dekad.controllers.Function;
import javafx.scene.chart.XYChart;

public class GraphManager {

    private final transient XYChart<Double, Double> chart;
    private double offset;

    public GraphManager(final XYChart<Double, Double> chart) {
        this.chart = chart;
        this.offset = 0.01;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(final double offset) {
        this.offset = offset;
    }

    public void plot(final Function function, final double min, final double max) {

        final XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (double x = min; x <= max; x += offset) {

            plotPoint(x, function.getMathFunction().eval(x), series);

        }

        chart.getData().add(series);

    }

    private void plotPoint(final double x, final double y, final XYChart.Series<Double, Double> series) {

        series.getData().add(new XYChart.Data<>(x, y));

    }

    public void clear() {

        chart.getData().clear();

    }
}
