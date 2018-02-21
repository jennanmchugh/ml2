package Part2;

import java.awt.*;

/**
 * For use with drawing structures from a sequence.
 */
public enum Direction {
    START (new Point(0, 0)),
    LEFT (new Point(-1, 0)),
    RIGHT (new Point(1, 0)),
    DOWN (new Point(0, -1)),
    UP (new Point(0, 1)),
    END (new Point(0, 0));

    Point point;

    Direction(Point point) { this.point = point; }

    public Point getPoint() { return point; }
}
