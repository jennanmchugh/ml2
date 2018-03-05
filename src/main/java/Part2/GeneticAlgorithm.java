package Part2;

import Part2.enums.Direction;
import Part2.io.ProteinReader;
import Part2.models.*;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created on 02/20/18 by Jenna McHugh.
 */
public class GeneticAlgorithm  {
    private static final Logger logger = LoggerFactory.getLogger(GeneticAlgorithm.class);
    private static Population population;
    private static final int MAX_GENERATIONS = 2000;

    private static final int MAX_FITNESS = 9;

    public static void main(String[] args) {
        start();
    }

    /**
     * This method will initialize population based on selected input sequence.
     */
    public static List<Structure> start() {
        ProteinReader reader = new ProteinReader(new File("src/main/resources/Input.txt"));
        List<Protein> proteins = reader.getProteins();

        List<Structure> bestGenStructures = new ArrayList<>();

        Structure[] firstGen = new Structure[200];
        //our initial input is the 4th index element in the proteins input file.
        for (int i = 0; i < 200; i++) {
            Structure s = selfAvoidingWalk(proteins.get(4));
            firstGen[i] = s;
        }
        population = new Population(firstGen);
        computeFitness(population);
        Arrays.sort(firstGen);
        bestGenStructures.add(firstGen[0]);

        Structure[] nextGen = new Structure[200];
        /*
        Program stop conditions:
        1.) is i == MAX_GENERATIONS (2000)?
        OR
        2.) is i == the maximum fitness level?
         */
        for (int i = 0; i < MAX_GENERATIONS; i++) {
            //take the 1st 10 elements of Gen. 1 (bc they have the highest fitness value) for elitism.
            for (int j = 0; j < 10; j++) {
                nextGen[j] = firstGen[j];
            }
            //for structures 10 - 169, we apply the crossover method
            for (int j = 10; j < 170; j++) {
                Structure structure1 = rouletteWheelSelection(firstGen);
                Structure structure2 = rouletteWheelSelection(firstGen);
                nextGen[j] = crossOver(structure1, structure2);
            }
            //choose randomly for the rest of the population
            for (int j = 170; j < 200; j++) {
                nextGen[j] = firstGen[new Random().nextInt(firstGen.length)];
            }
            population = new Population(nextGen);
            computeFitness(population);
            Arrays.sort(nextGen);
            bestGenStructures.add(nextGen[0]);
            if (nextGen[0].getFitness().getTotalFitness() == MAX_FITNESS) {
                break;
            }
        }

        return bestGenStructures;
    }

    /**
     * This method implements the self avoiding walk. It always chooses (0,0) as position 1, and (1,0) as position 2. From then on, the next structureNodes are
     * chosen randomly based on available directions. Fails/ structure deemed invalid if there are no more available structureNodes.
     * @param protein the sequence we are drawing a structure from.
     */
    private static Structure selfAvoidingWalk(Protein protein) {
        List<Point> visited = new ArrayList<>();
        List<StructureNode> nodes = new ArrayList<>();
        //we always start at the origin (0,0)
        nodes.add(new StructureNode(protein.getSequence().get(0).getLetter(), new Point(0, 0)));
        visited.add(new Point(0, 0));

        //we always place the second at (1,0)
        nodes.add(new StructureNode(protein.getSequence().get(1).getLetter(), new Point(1, 0)));
        visited.add(new Point(1, 0));

        int i = 2;
        while (i < protein.getSequence().size()) {
            Point curr = nodes.get(nodes.size()-1).getPosition();

            //all adjacent points to the previous point.
            List<Point> possibleMoves = new ArrayList<>();
            Point nextRight = addPoints(curr, Direction.RIGHT.getPoint());
            Point nextLeft = addPoints(curr, Direction.LEFT.getPoint());
            Point nextUp = addPoints(curr, Direction.UP.getPoint());
            Point nextDown = addPoints(curr, Direction.DOWN.getPoint());
            //if this point hasn't been visited yet, we can add it to our possible moves.
            if (!visited.contains(nextRight)) { possibleMoves.add(nextRight); }
            if (!visited.contains(nextLeft)) { possibleMoves.add(nextLeft); }
            if (!visited.contains(nextUp)) { possibleMoves.add(nextUp); }
            if (!visited.contains(nextDown)) { possibleMoves.add(nextDown); }

        /*
        occasionally we will run into an issue where the points in each direction from the current point have already been visited.
        if this is the case, we have 0 possible moves. we have to erase our current point from the nodes and the visited points, and go back by one position.
        we also remove our current point from the list of options.
        for example:
            Say our current position (value of Point curr) is (4,0). nextRight (4,1), nextLeft(3,0), nextUp (4,2), and nextDown (4,-1) have all been visited.
            We have no possible moves. In the while loop, we remove (4,0) from the nodes and visited list. Then we get the point at the previous position and get all points in the same direction.
            All points that haven't been visited yet & aren't equal to (4,0) are added to the possible moves.
         */
            while (possibleMoves.size() == 0) {
                Point current = nodes.get(nodes.size()-1).getPosition();
                visited.remove(current);
                boolean removed = nodes.removeIf(n -> n.getPosition().equals(current));
                if (removed) {
                    Point prev = nodes.get(nodes.size()-1).getPosition();
                    Point right = addPoints(prev, Direction.RIGHT.getPoint());
                    Point left = addPoints(prev, Direction.LEFT.getPoint());
                    Point up = addPoints(prev, Direction.UP.getPoint());
                    Point down = addPoints(prev, Direction.DOWN.getPoint());
                    if (!right.equals(current) && !visited.contains(right)) { possibleMoves.add(right); }
                    if (!left.equals(current) && !visited.contains(left)) { possibleMoves.add(left); }
                    if (!up.equals(current) && !visited.contains(up)) { possibleMoves.add(up); }
                    if (!down.equals(current) && !visited.contains(down)) { possibleMoves.add(down); }
                    i--;
                }
                else {
                    logger.error("error removing point (" + current.getX() + "," + current.getY() +")");
                    System.exit(0);
                }
            }

            if (possibleMoves.size() > 0) {
                Point next = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
                nodes.add(new StructureNode(protein.getSequence().get(i).getLetter(), next));
                visited.add(next);
                i++;
            }
        }

        return new Structure(protein.getFormattedSequenceString(protein.getSequence()), new Fitness(0, new ArrayList<>()), nodes, visited);
    }

    /**
     * Computes fitness value of structureNode. This number is also known as "topological neighbors" (TN): the number of neighboring H-H contacts where the
     * H's are not already covalently bonded or sequentially connected within the sequence.
     */
    private static void computeFitness(Population population) {
        for (Structure s : population.getStructures()) {
            int fitness;
            List<Pair<Point, Point>> bonds = new ArrayList<>();
            //For fitness, we only care about H-H bonds. So let's start with grabbing only the hydrophobic structureNodes from the structureNode.
            List<StructureNode> hydrophobics = new ArrayList<>();
            for (StructureNode n : s.getNodes()) {
                if (n.getAminoAcid().equalsIgnoreCase("h")) {
                    hydrophobics.add(n);
                }
            }
            Collections.sort(hydrophobics);
            for (int i = 0; i < hydrophobics.size(); i++) {
                Point current = hydrophobics.get(i).getPosition();
                int currentIndex = s.getNodes().indexOf(new StructureNode("h", current));
                for (int j = 0; j < hydrophobics.size(); j++) {
                    Point compare = hydrophobics.get(j).getPosition();
                    int compareIndex = s.getNodes().indexOf(new StructureNode("h", compare));
                    if (i != j) {
                        if ((current.x == compare.x && Math.abs(compareIndex - currentIndex) != 1 && Math.abs(current.y - compare.y) == 1) || (current.y == compare.y && Math.abs(compareIndex - currentIndex) != 1 && Math.abs(current.x - compare.x) == 1 )) {
                            if (!bonds.contains(new Pair<>(current, compare)) && !bonds.contains(new Pair<>(compare, current))) {
                                bonds.add(new Pair<>(current, compare));
                            }
                        }
                    }
                }
            }
            fitness = bonds.size();

            s.setFitness(new Fitness(fitness, bonds));
        }
    }

    /**
     * Used for the self avoiding walk. This determines the new location of the acid by taking the previous location and adding it to the previous direction.
     * @param p1 Previous point position.
     * @param p2 Previous location's noted direction.
     * @return the new Point value after adding the two points together.
     */
    private static Point addPoints(Point p1, Point p2) {
        return new Point((int)(p1.getX() + p2.getX()), (int)(p1.getY() + p2.getY()));
    }

    /**
     * The Roulette wheel selection. Favors structures with a higher fitness level.
     * @param structures an array of structures
     * @return a structure based on the roulette wheel selection from a list of structures
     */
    private static Structure rouletteWheelSelection(Structure[] structures) {
        int sum = 0;
        int partial = 0;
        for (Structure struct : structures) {
            sum += struct.getFitness().getTotalFitness();
        }
        int bound = new Random().nextInt(sum);

        for (Structure s: structures) {
            partial += s.getFitness().getTotalFitness();
            if (partial > bound) {
                return s;
            }
        }

        return null;
    }


    private static Structure crossOver(Structure structure1, Structure structure2) {
        Structure s = new Structure();
        boolean structureValid = false;
        while (!structureValid) {
            List<StructureNode> nodes = new ArrayList<>();
            List<Point> visited = new ArrayList<>();
            int randomPosition = new Random().nextInt(structure1.getNodes().size()-3) + 1;
            if (structure1.getNodes().get(randomPosition-1).getPosition().distance(structure2.getNodes().get(randomPosition).getPosition()) == 1) {
                for (int i = 0; i < randomPosition; i++) {
                    nodes.add(structure1.getNodes().get(i));
                    visited.add(nodes.get(i).getPosition());
                }
                for (int i = randomPosition; i < structure2.getNodes().size(); i++) {
                    nodes.add(structure2.getNodes().get(i));
                    visited.add(nodes.get(i).getPosition());
                }

                s = new Structure(structure1.getSequence(), new Fitness(0, new ArrayList<>()), nodes, visited);
                structureValid = validateStructure(s, randomPosition, structure1, structure2);
            }

        }

        return s;
    }

    private static boolean validateStructure(Structure structure, int index, Structure parent1, Structure parent2) {
        for (int i = index; i < parent2.getNodes().size(); i++) {
            for (int j = 0; j < index; j++) {
                if (parent1.getVisitedPoints().get(j).equals(parent2.getNodes().get(i).getPosition())) {
                    //this point has already been visited in the first half from parent 1.
                    return false;
                }
            }
        }

        return true;
    }

}
