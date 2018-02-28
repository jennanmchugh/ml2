package Part2.models;

import javafx.util.Pair;

import java.awt.*;
import java.util.List;

public class Fitness implements Comparable<Fitness> {
    private int totalFitness;
    private List<Pair<Point, Point>> points;

    public int getTotalFitness() {
        return totalFitness;
    }

    public void setTotalFitness(int totalFitness) {
        this.totalFitness = totalFitness;
    }

    public List<Pair<Point, Point>> getPoints() {
        return points;
    }

    public void setPoints(List<Pair<Point, Point>> points) {
        this.points = points;
    }


    public Fitness(int totalFitness, List<Pair<Point, Point>> points) {
        this.totalFitness = totalFitness;
        this.points = points;
    }

    @Override
    public int compareTo(Fitness other) {
        return Integer.compare(this.getTotalFitness(), other.getTotalFitness());
    }

}
