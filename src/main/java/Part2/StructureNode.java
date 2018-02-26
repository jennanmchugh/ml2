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

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + aminoAcid.hashCode();
        result = 31 * result + position.x;
        result = 31 * result + position.y;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if  (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        StructureNode other = (StructureNode) obj;
        if (!aminoAcid.equals(other.aminoAcid)) { return false; }
        if (position != other.position) { return false; }

        return true;
    }

}
