package dekad.core;

import javafx.scene.chart.XYChart;
import java.util.function.Function;

public class Graph {

    private XYChart<Double, Double> graph;

    private double offset;

    public Graph(final XYChart<Double, Double> graph) {
        this.graph = graph;
        this.offset = 0.01;
    }

    public Graph(final XYChart<Double, Double> graph, double offset) {
        this.graph = graph;
        this.offset = offset;
    }

    public void plot(final Function<Double, Double> function, double min, double max) {

        final XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for(double x = min; x <= max; x += offset) {
            plotPoint(x, function.apply(x), series);
        }

        graph.getData().add(series);

    }

    public void plotPoint(final double x, final double y, final XYChart.Series<Double, Double> series) {

        series.getData().add(new XYChart.Data<>(x, y));

    }

    public void clear() {

        graph.getData().clear();

    }

}
