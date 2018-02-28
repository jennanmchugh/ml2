package Part2.models;

public class AminoAcid {

    private String letter;
    private boolean isHydrophobic;

    public boolean isHydrophobic() {
        return isHydrophobic;
    }

    public String getLetter() {
        return letter;
    }

    public AminoAcid(String letter, boolean isHydrophobic) {
        this.letter = letter;
        this.isHydrophobic = isHydrophobic;
    }
}
