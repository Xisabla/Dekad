package dekad.models;

import javafx.scene.chart.XYChart;

public class GraphManager {

    private final XYChart<Double, Double> chart;
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

    public void plot(final MathFunction mathFunction, final double min, final double max) {

        final XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (double x = min; x <= max; x += offset) {

            // plotPoint(x, mathFunction.eval(x), series);
            series.getData().add(new XYChart.Data<>(x, mathFunction.eval(x)));

        }

        chart.getData().add(series);

    }

    public void clear() {

        chart.getData().clear();

    }
}
