package Part2;

import java.util.List;

public class Population {
    private List<Structure> structures;

    public List<Structure> getStructures() { return structures; }

    public void setStructures(List<Structure> structures) { this.structures = structures; }

    public Population(List<Structure> structures) {
        this.structures = structures;
    }
}
