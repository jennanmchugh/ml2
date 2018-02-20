package Part2;

public class AminoAcid {

    private String letter;
    private boolean isHydrophobic;

    public boolean isHydrophobic() {
        return isHydrophobic;
    }

    public String getLetter() {
        return letter;
    }

    AminoAcid(String letter, boolean isHydrophobic) {
        this.letter = letter;
        this.isHydrophobic = isHydrophobic;
    }
}
