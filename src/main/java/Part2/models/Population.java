package Part2.models;


public class Population {
    private Structure[] structures;

    public Structure[] getStructures() { return structures; }

    public void setStructures(Structure[] structures) { this.structures = structures; }

    public Population(Structure[] structures) {
        this.structures = structures;
    }
}
