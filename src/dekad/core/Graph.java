package dekad.core;

import dekad.models.MathFunction;
import javafx.scene.chart.XYChart;

public class Graph {

    private static Graph single_instance = null;
    private XYChart<Double, Double> chart;
    private double offset;

    public Graph(final XYChart<Double, Double> chart) {
        this.chart = chart;
        this.offset = 0.01;
        Graph.single_instance = this;
    }

    public Graph(final XYChart<Double, Double> chart, double offset) {
        this.chart = chart;
        this.offset = offset;
        Graph.single_instance = this;
    }

    public XYChart<Double, Double> getChart() {
        return chart;
    }

    public void setChart(XYChart<Double, Double> chart) {
        this.chart = chart;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public void plot(MathFunction mathFunction, double min, double max) {

        final XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (double x = min; x <= max; x += offset) {

            plotPoint(x, mathFunction.eval(x), series);

        }

        chart.getData().add(series);

    }

    public void plotPoint(final double x, final double y, final XYChart.Series<Double, Double> series) {

        series.getData().add(new XYChart.Data<>(x, y));

    }

    public void clear() {

        chart.getData().clear();

    }

    public static Graph getInstance() {

        if (single_instance == null)
            single_instance = new Graph(null, 0.01);

        return single_instance;

    }

    public static Graph getInstance(final XYChart<Double, Double> chart) {

        Graph instance = getInstance();

        instance.setChart(chart);

        return instance;

    }

}
