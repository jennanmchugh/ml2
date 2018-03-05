package Part2.fx;

import Part2.GeneticAlgorithm;
import Part2.models.Structure;
import Part2.models.StructureNode;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import java.awt.*;

public class StartProgram extends Application {
    private static final Logger logger = LoggerFactory.getLogger(StartProgram.class);

    @Override
    public void start(Stage primaryStage) {
        drawStructure(GeneticAlgorithm.start(), primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }


    private void drawStructure(List<Structure> genStructures, Stage primaryStage) {
        int i = 0;
            final NumberAxis xAxis = new NumberAxis(-20, 20, 1);
            final NumberAxis yAxis = new NumberAxis(-20, 20, 1);
            final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
            lineChart.setTitle("Generation: "+i+" | Sequence: " + genStructures.get(i).getSequence() + " | Fitness: " + genStructures.get(i).getFitness().getTotalFitness());
            XYChart.Series series = new XYChart.Series();
            for (StructureNode n : genStructures.get(i).getNodes()) {
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
                for (StructureNode sn : genStructures.get(i).getNodes()) {
                    if (sn.getPosition().equals(chartPoint)) {
                        //hydrophobic bonds drawn as green
                        if (sn.getAminoAcid().equals("h")) {
                            Node point = d.getNode().lookup(".chart-line-symbol");
                            Color hydrophobic = Color.GREEN;
                            String rgb = String.format("%d, %d, %d", (int)(hydrophobic.getRed() * 255), (int)(hydrophobic.getGreen() * 255), (int)(hydrophobic.getBlue() * 255));
                            point.setStyle("-fx-background-color: rgba(" + rgb + ",1.0);");
                        }
                        //hydrophilic bonds drawn as blue
                        else {
                            Node point = d.getNode().lookup(".chart-line-symbol");
                            Color hydrophilic = Color.BLUE;
                            String rgb = String.format("%d, %d, %d", (int)(hydrophilic.getRed() * 255), (int)(hydrophilic.getGreen() * 255), (int)(hydrophilic.getBlue() * 255));
                            point.setStyle("-fx-background-color: rgba(" + rgb + ",1.0);");
                        }
                    }
                }
            }
            for (int j = 0; j < data.size()-1; j++) {
                Double x = (double)data.get(j).getXValue();
                Double y = (double)data.get(j).getYValue();
                Point dataPtVal = new Point(x.intValue(), y.intValue());
                if (genStructures.get(i).getFitness().getPoints().contains(dataPtVal)) {
                    Node line = series.getNode().lookup(".chart-series-line");
                    Color c = Color.RED;
                    String rgb = String.format("%d, %d, %d", (int)(c.getRed() * 255), (int)(c.getGreen() * 255), (int)(c.getBlue() * 255));
                    line.setStyle("-fx-stroke: rgba(" + rgb + " , 1.0);");
                    primaryStage.setTitle("CSCI 6990 Spring 2018 Program 1");
                    primaryStage.setScene(scene);
                    primaryStage.show();
                }
            }
            Node line = series.getNode().lookup(".chart-series-line");
            Color c = Color.BLACK;
            String rgb = String.format("%d, %d, %d", (int)(c.getRed() * 255), (int)(c.getGreen() * 255), (int)(c.getBlue() * 255));
            line.setStyle("-fx-stroke: rgba(" + rgb + " , 1.0);");
            primaryStage.setTitle("CSCI 6990 Spring 2018 Program 1");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    }

