package Part2;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Fitness implements Comparable<Fitness> {
    private int totalFitness;
    private HashSet<Point> points;

    public int getTotalFitness() {
        return totalFitness;
    }

    public void setTotalFitness(int totalFitness) {
        this.totalFitness = totalFitness;
    }

    public Set<Point> getPoints() {
        return points;
    }

    public void setPoints(HashSet<Point> points) {
        this.points = points;
    }


    public Fitness(int totalFitness, HashSet<Point> points) {
        this.totalFitness = totalFitness;
        this.points = points;
    }

    @Override
    public int compareTo(Fitness other) {
        return Integer.compare(this.getTotalFitness(), other.getTotalFitness());
    }

}
