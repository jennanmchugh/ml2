package Part2;

import javafx.util.Pair;

import java.awt.*;
import java.util.List;

public class Structure implements Comparable<Structure> {
    private String sequence;
    private int fitness;
    private List<Pair<Point, Point>> fitnessBond;
    private List<StructureNode> nodes;
    private List<Point> visitedPoints;


    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public List<StructureNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<StructureNode> nodes) {
        this.nodes = nodes;
    }

    public List<Point> getVisitedPoints() { return visitedPoints; }

    public void setVisitedPoints(List<Point> visitedPoints) { this.visitedPoints = visitedPoints; }

    public List<Pair<Point, Point>> getFitnessBond() { return fitnessBond; }

    public void setFitnessBond(List<Pair<Point, Point>> fitnessBond) { this.fitnessBond = fitnessBond;}

    public Structure(String sequence, int fitness, List<StructureNode> nodes, List<Point> visitedPoints) {
        this.sequence = sequence;
        this.fitness = fitness;
        this.nodes = nodes;
        this.visitedPoints = visitedPoints;
    }

    @Override
    public int compareTo(Structure other) {
        return Integer.compare(this.fitness, other.fitness);
    }
}
