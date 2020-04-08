package dekad.core;

import javafx.scene.chart.XYChart;
import org.mariuszgromada.math.mxparser.Expression;

public class Graph {

    private static Graph single_instance = null;
    private XYChart<Double, Double> graph;
    private double offset;

    public Graph(final XYChart<Double, Double> graph) {
        this.graph = graph;
        this.offset = 0.01;
        Graph.single_instance = this;
    }

    public Graph(final XYChart<Double, Double> graph, double offset) {
        this.graph = graph;
        this.offset = offset;
        Graph.single_instance = this;
    }

    public XYChart<Double, Double> getGraph() {
        return graph;
    }

    public void setGraph(XYChart<Double, Double> graph) {
        this.graph = graph;
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public void plot(final Expression e, double min, double max) {

        final XYChart.Series<Double, Double> series = new XYChart.Series<>();

        for (double x = min; x <= max; x += offset) {

            e.setArgumentValue("x", x);
            plotPoint(x, e.calculate(), series);

        }

        graph.getData().add(series);

    }

    public void plotPoint(final double x, final double y, final XYChart.Series<Double, Double> series) {

        series.getData().add(new XYChart.Data<>(x, y));

    }

    public void clear() {

        graph.getData().clear();

    }

    public static Graph getInstance() {

        if (single_instance == null)
            single_instance = new Graph(null, 0.01);

        return single_instance;

    }

}
