package Part2.fx;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class RunApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group group = new Group();
        Pane pane = new Pane(group);

        pane.setPrefWidth(300);
        pane.setPrefHeight(300);

        group.getChildren().add(new Circle(0, 0, 10));
        group.setTranslateX(pane.getPrefWidth() / 2);
        group.setTranslateY(pane.getPrefHeight() / 2);
        //make traditional Y coords.
        group.setScaleY(-1);

        primaryStage.setScene(new Scene(group));
        primaryStage.show();
    }
}
