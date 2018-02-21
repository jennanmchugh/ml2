package Part2;

import java.awt.*;

public class Move {

    private String aminoAcid;
    private Point position;
    private Direction direction;

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

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Move(String aminoAcid, Point position, Direction direction) {
        this.aminoAcid = aminoAcid;
        this.position = position;
        this.direction = direction;
    }
}
