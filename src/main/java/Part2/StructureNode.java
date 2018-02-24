package Part2;

import java.awt.*;

public class StructureNode implements Comparable<StructureNode> {

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


    public StructureNode(String aminoAcid, Point position) {
        this.aminoAcid = aminoAcid;
        this.position = position;
    }

    //override the "compareTo" method to sort the points by their x-values.
    @Override
    public int compareTo(StructureNode structureNode) {
        return Integer.compare((int)this.position.getX(), (int)structureNode.getPosition().getX());
    }
}
