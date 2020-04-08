package dekad.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class App implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(location);
    }

}
/*

import dekad.core.beta.Graph;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Double.NaN;
import static java.lang.Double.isNaN;
import static java.lang.Math.PI;
import static java.lang.Math.sin;

public class App implements Initializable {
    @FXML
    private LineChart<Double, Double> lineGraph;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private Graph mathsGraph;

    private double lastDragX;
    private double lastDragY;
    private double xmin;
    private double xmax;
    private double ymin;
    private double ymax;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        xmin = -10;
        xmax = 10;
        ymin = -0.8;
        ymax = 2.1;
        lastDragX = NaN;
        lastDragY = NaN;

        lineGraph.setAnimated(false);

        xAxis.setLowerBound(xmin);
        xAxis.setUpperBound(xmax);
        yAxis.setLowerBound(ymin);
        yAxis.setUpperBound(ymax);

        mathsGraph = new Graph(lineGraph, 0.01);

        lineGraph.setVisible(true);
        plotLine(xmin, xmax);

        lineGraph.getParent().setOnScrollFinished(event -> {
                System.out.println(event);
                mathsGraph.setOffset(0.01);
                mathsGraph.clear();
                plotLine(xmin, xmax);
            });

        lineGraph.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                System.out.println(event);

                mathsGraph.clear();
                mathsGraph.setOffset(0.1);

                if (event.getDeltaY() > 0) {

                    xmin *= 0.7;
                    xmax *= 0.7;
                    ymin *= 0.7;
                    ymax *= 0.7;

                } else {

                    xmin /= 0.7;
                    xmax /= 0.7;
                    ymin /= 0.7;
                    ymax /= 0.7;
                }

                xAxis.setLowerBound(xmin);
                xAxis.setUpperBound(xmax);
                yAxis.setLowerBound(ymin);
                yAxis.setUpperBound(ymax);

                plotLine(xmin, xmax);
            }
        });

        lineGraph.setOnMouseReleased(event -> {
            lastDragX = NaN;
            mathsGraph.setOffset(0.1);
            mathsGraph.clear();
            plotLine(xmin, xmax);
        });

        lineGraph.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!isNaN(lastDragX) && !isNaN(lastDragY)) {
                    double diffX = (lastDragX - event.getX())/85;
                    double diffY = -(lastDragY - event.getY())/85;

                    // System.out.println(diffX);

                    mathsGraph.clear();
                    mathsGraph.setOffset(1);

                    xmin += diffX;
                    xmax += diffX;
                    ymin += diffY;
                    ymax += diffY;

                    xAxis.setLowerBound(xmin);
                    xAxis.setUpperBound(xmax);
                    yAxis.setLowerBound(ymin);
                    yAxis.setUpperBound(ymax);

                    plotLine(xmin, xmax);

                    lastDragX = event.getX();
                    lastDragY = event.getY();
                }

                lastDragX = event.getX();
                lastDragY = event.getY();

                // System.out.println(event);
            }
        });
    }

    private void plotLine(double min, double max) {

        mathsGraph.plotLine(x -> {
            if(x == 0) return (double) 1;
            return sin(2*PI*x)/(PI*x);
        }, min, max);

    }

}
*/
