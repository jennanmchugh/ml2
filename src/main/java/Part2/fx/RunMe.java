package Part2.fx;

import Part2.GeneticAlgorithm;
import Part2.Structure;
import Part2.StructureNode;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.awt.*;

public class RunMe extends Application {

    @Override
    public void start(Stage primaryStage) {
        //TODO: need to highlight tn bonds. need to animate all generations.

        Structure s = GeneticAlgorithm.initPopulation();

        final NumberAxis xAxis = new NumberAxis(-20, 20, 1);
        final NumberAxis yAxis = new NumberAxis(-20, 20, 1);


        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        lineChart.setTitle("Generation: 1 | Sequence: " + s.getSequence() + " | Fitness: " + s.getFitness());
        XYChart.Series series = new XYChart.Series();
        for (StructureNode n : s.getNodes()) {
            if (s.getFitnessBond().contains(n.getPosition())) {
                //grab the key and value from the bonds list and change line stroke here to display a fitness bond.

            }
            series.getData().add(new XYChart.Data<>(n.getPosition().getX(), n.getPosition().getY()));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().addAll(series);

        ObservableList<XYChart.Data> data = series.getData();
        for (XYChart.Data d: data) {
            Double x = (double)d.getXValue();
            Double y = (double)d.getYValue();
            Point chartPoint = new Point(x.intValue(), y.intValue());
            //style the points based on which acid they are
            for (StructureNode sn : s.getNodes()) {
                if (sn.getPosition().equals(chartPoint)) {
                    if (sn.getAminoAcid().equals("h")) {
                        Node point = d.getNode().lookup(".chart-line-symbol");
                        Color hydrophobic = Color.GREEN;
                        String rgb = String.format("%d, %d, %d", (int)(hydrophobic.getRed() * 255), (int)(hydrophobic.getGreen() * 255), (int)(hydrophobic.getBlue() * 255));
                        point.setStyle("-fx-background-color: rgba(" + rgb + ",1.0);");
                    }
                    else {
                        Node point = d.getNode().lookup(".chart-line-symbol");
                        Color hydrophilic = Color.BLUE;
                        String rgb = String.format("%d, %d, %d", (int)(hydrophilic.getRed() * 255), (int)(hydrophilic.getGreen() * 255), (int)(hydrophilic.getBlue() * 255));
                        point.setStyle("-fx-background-color: rgba(" + rgb + ",1.0);");
                    }
                }
            }


        }

        Node line = series.getNode().lookup(".chart-series-line");
        Color c = Color.RED;
        String rgb = String.format("%d, %d, %d", (int)(c.getRed() * 255), (int)(c.getGreen() * 255), (int)(c.getBlue() * 255));
        line.setStyle("-fx-stroke: rgba(" + rgb + " , 1.0);");

        primaryStage.setTitle("Line chart drawing.");
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void stylePoints(ObservableList<Event> events, final LineChart<Number, Number> lineChart) {
        lineChart.applyCss();

    }
}
