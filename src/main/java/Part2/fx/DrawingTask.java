package Part2.fx;

import Part2.models.Structure;
import Part2.models.StructureNode;
import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class DrawingTask<T> extends AnimationTimer {
    private final AtomicReference<T> data = new AtomicReference<>(null);
    private final LineChart<Number, Number> lineChart;

    public DrawingTask(LineChart<Number, Number> lineChart) {
        this.lineChart = lineChart;
    }

    public void requestRedraw(T dataToDraw) {
        data.set(dataToDraw);
        start();
    }

    public void handle(long now) {
        T dataToDraw = data.getAndSet(null);
        if (dataToDraw != null) {
          //  redraw(canvas.getGraphicsContext2D(), dataToDraw);
        }
    }

    public final void draw(LineChart<Number, Number> linechart, Structure structure, int genNumber) {
        XYChart.Series series = new XYChart.Series();
        linechart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        linechart.setTitle("Generation: "+genNumber+" | Sequence: " + structure.getSequence() + " | Fitness: " + structure.getFitness().getTotalFitness());

        //clear linechart data
        series.getData().clear();

        for (StructureNode n : structure.getNodes()) {
            series.getData().add(new XYChart.Data<>(n.getPosition().getX(), n.getPosition().getY()));
        }
        linechart.getData().addAll(series);
        ObservableList<XYChart.Data> data = series.getData();
        for (XYChart.Data d: data) {
            Double x = (double)d.getXValue();
            Double y = (double)d.getYValue();
            Point chartPoint = new Point(x.intValue(), y.intValue());
            //style the points based on which acid they are
            for (StructureNode sn : structure.getNodes()) {
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
            if (structure.getFitness().getPoints().contains(dataPtVal)) {
                Node line = series.getNode().lookup(".chart-series-line");
                Color c = Color.RED;
                String rgb = String.format("%d, %d, %d", (int)(c.getRed() * 255), (int)(c.getGreen() * 255), (int)(c.getBlue() * 255));
                line.setStyle("-fx-stroke: rgba(" + rgb + " , 1.0);");
            }
        }
        Node line = series.getNode().lookup(".chart-series-line");
        Color c = Color.BLACK;
        String rgb = String.format("%d, %d, %d", (int)(c.getRed() * 255), (int)(c.getGreen() * 255), (int)(c.getBlue() * 255));
        line.setStyle("-fx-stroke: rgba(" + rgb + " , 1.0);");
    }
}
