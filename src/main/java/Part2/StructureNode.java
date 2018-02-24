package Part2;

import java.awt.*;

public class Move {

    private String aminoAcid;
    private Point position;

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public String getAminoAcid() {
        return aminoAcid;
    }

    public void setAminoAcid(String aminoAcid) {
        this.aminoAcid = aminoAcid;
    }


    public Move(String aminoAcid, Point position) {
        this.aminoAcid = aminoAcid;
        this.position = position;
    }
}
