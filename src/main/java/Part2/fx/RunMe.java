package Part2.fx;

import Part2.GeneticAlgorithm;
import Part2.Structure;
import Part2.StructureNode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;



public class RunMe extends Application {

    @Override
    public void start(Stage primaryStage) {
        //TODO: need to update acids to have different colors depending on type. need to highlight tn bonds. need to animate all generations. 

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
        primaryStage.setTitle("Line chart drawing.");
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
