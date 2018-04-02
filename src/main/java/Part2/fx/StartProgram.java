package Part2.fx;

import Part2.GeneticAlgorithm;
import Part2.models.Structure;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class StartProgram extends Application {
    private static final Logger logger = LoggerFactory.getLogger(StartProgram.class);
    private static final List<Structure> structures = new GeneticAlgorithm().getStructures();
    private Thread thread = null;
    private int i = 0;


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CSCI 6990 | Spring 2018 | Program #1");
        NumberAxis xAxis = new NumberAxis(-20, 20, 1);
        NumberAxis yAxis = new NumberAxis(-20, 20, 1);
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        Button button = new Button("Start");

        button.setOnAction(event -> {
            if (thread != null) {
                thread.stop();
            }

            button.setText("Draw Structure: " + i+1);
            DrawStructure drawStructure = new DrawStructure(structures.get(i++), lineChart, i);
            thread = new Thread(drawStructure);
            thread.setDaemon(true);
            thread.start();
        });

        StackPane spLineChart = new StackPane();
        spLineChart.getChildren().add(lineChart);

        StackPane spButton = new StackPane();
        spButton.getChildren().add(button);

        VBox vBox = new VBox();
        vBox.setVgrow(spLineChart, Priority.ALWAYS);
        vBox.getChildren().addAll(spLineChart, spButton);

        Scene scene = new Scene(vBox, 800, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public void startDrawing() {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                drawStructure();
            }
        };
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void drawStructure() { }

    }

