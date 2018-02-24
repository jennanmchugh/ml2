package Part2;

import java.util.List;

public class Structure implements Comparable<Structure> {
    private String sequence;
    private int fitness;
    private List<StructureNode> nodes;


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

    public Structure(String sequence, int fitness, List<StructureNode> nodes) {
        this.sequence = sequence;
        this.fitness = fitness;
        this.nodes = nodes;
    }

    @Override
    public int compareTo(Structure other) {
        return Integer.compare(this.fitness, other.fitness);
    }
}
