package Part2.fx;

import Part2.models.Structure;
import javafx.application.Platform;
import javafx.scene.chart.LineChart;

public class DrawStructure implements Runnable {

    private final Structure structure;
    private final LineChart<Number, Number> lineChart;
    private final int generation;
    private boolean run;

    public DrawStructure(Structure structure, LineChart<Number, Number> lineChart, int generation) {
        this.structure = structure;
        this.lineChart = lineChart;
        this.generation = generation;
        this.run = true;
    }

    @Override
    public void run() {
        DrawingTask<Structure> drawingTask = new DrawingTask<>(lineChart);
            Platform.runLater(() -> drawingTask.draw(lineChart, structure, generation));
    }

}
