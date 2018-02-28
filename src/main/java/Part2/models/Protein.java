package Part2.models;

import java.util.List;

public class Protein {
    private List<AminoAcid> sequence;
    private int fitness;

    public Protein(List<AminoAcid> sequence, int fitness) {
        this.sequence = sequence;
        this.fitness = fitness;
    }

    public List<AminoAcid> getSequence() { return sequence; }

    public void setSequence(List<AminoAcid> sequence) { this.sequence = sequence; }

    public int getFitness() { return fitness; }

    public void setFitness(int fitness) { this.fitness = fitness; }

    public String getFormattedSequenceString(List<AminoAcid> sequence) {
        StringBuilder sb = new StringBuilder();
        for (AminoAcid a : sequence) {
            sb.append(a.getLetter());
        }

        return sb.toString();
    }

}
