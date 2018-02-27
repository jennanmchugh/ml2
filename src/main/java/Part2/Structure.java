package Part2;


import java.awt.*;
import java.util.List;

public class Structure implements Comparable<Structure>{
    private String sequence;
    private Fitness fitness;
    private List<StructureNode> nodes;
    private List<Point> visitedPoints;


    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Fitness getFitness() {
        return fitness;
    }

    public void setFitness(Fitness fitness) {
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


    public Structure(String sequence, Fitness fitness, List<StructureNode> nodes, List<Point> visitedPoints) {
        this.sequence = sequence;
        this.fitness = fitness;
        this.nodes = nodes;
        this.visitedPoints = visitedPoints;
    }

    public Structure(String sequence) { this.sequence = sequence; }

    @Override
    public int compareTo(Structure other) {
        return Integer.compare(other.fitness.getTotalFitness(), this.fitness.getTotalFitness());
    }
}
