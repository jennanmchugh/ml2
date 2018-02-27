package Part2;

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
        Protein p = proteins.get(4);
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
        for (int i = 0; i < MAX_GENERATIONS && i < MAX_FITNESS; i++) {
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
            //apply mutation to the next 10 chromosomes
            for (int j = 170; j < 180; j++) {
                //mutate
            }
            //choose randomly for the rest of the population
            for (int j = 180; j < 200; j++) {
                nextGen[j] = firstGen[new Random().nextInt(firstGen.length)];
            }
            population = new Population(nextGen);
            computeFitness(population);
            Arrays.sort(nextGen);
            bestGenStructures.add(nextGen[0]);
            //let's see if we can make it here without it breaking
            System.out.println("we made it yall");
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

        //start i counter at 2 since we have already set the values for positions 0 & 1.
        for (int i = 2; i < protein.getSequence().size(); i++) {
            randomOrientation(i, protein.getSequence().get(i).getLetter(), nodes, visited);
        }

        logger.info("Moves: ");
        for (StructureNode n : nodes) {
            logger.info(n.getAminoAcid() + " (" + n.getPosition().getX() + "," + n.getPosition().getY() + ")");
        }

        return new Structure(protein.getFormattedSequenceString(protein.getSequence()), new Fitness(0, new HashSet<Point>()), nodes, visited);
    }

    /**
     * This is where our self-avoiding walk magic is actually happening.
     * @param moveNum Our index position in the structure
     * @param acid The letter representing the current acid - H for hydrophobic, P for hydrophilic/polar
     * @param nodes The structure nodes that have been added so far to the structure
     * @param visited All the points that have been visited.
     */
    private static void randomOrientation(int moveNum, String acid, List<StructureNode> nodes, List<Point> visited) {
        //check prev position
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
            Point prev = nodes.get(nodes.size()-2).getPosition();
            Point right = addPoints(prev, Direction.RIGHT.getPoint());
            Point left = addPoints(prev, Direction.LEFT.getPoint());
            Point up = addPoints(prev, Direction.UP.getPoint());
            Point down = addPoints(prev, Direction.DOWN.getPoint());
            if ( right.getX() != curr.getX() && right.getY() != curr.getY() && !visited.contains(right)) { possibleMoves.add(right); }
            if (left.getX() != curr.getX() && left.getY() != curr.getY() && !visited.contains(left)) { possibleMoves.add(left); }
            if (up.getX() != curr.getX() && up.getY() != curr.getY() && !visited.contains(up)) { possibleMoves.add(up); }
            if (down.getX() != curr.getX() && down.getY() != curr.getY() && !visited.contains(down)) { possibleMoves.add(down); }
        }

        Point next = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
        nodes.add(new StructureNode(acid, next));
        visited.add(next);
    }


    /**
     * Computes fitness value of structureNode. This number is also known as "topological neighbors" (TN): the number of neighboring H-H contacts where the
     * H's are not already covalently bonded or sequentially connected within the sequence.
     */
    private static void computeFitness(Population population) {
        for (Structure s : population.getStructures()) {
            int fitness = 0;
            //For fitness, we only care about H-H bonds. So let's start with grabbing only the hydrophobic structureNodes from the structureNode.
            List<StructureNode> hydrophobics = new ArrayList<>();
            for (StructureNode n : s.getNodes()) {
                if (n.getAminoAcid().equalsIgnoreCase("h")) {
                    hydrophobics.add(n);
                }
            }
            Collections.sort(hydrophobics);
            HashSet<Point> fitnessBonds = new HashSet<>();
            for (int i = 0; i < hydrophobics.size()-1; i++) {
                logger.info("("+hydrophobics.get(i).getPosition().getX()+","+hydrophobics.get(i).getPosition().getY()+")");
                Point curr = hydrophobics.get(i).getPosition();
                int currIndex = s.getNodes().indexOf(new StructureNode("h", curr));
                Point next = hydrophobics.get(i+1).getPosition();
                int nextIndex = s.getNodes().indexOf(new StructureNode("h", next));
                //if the distance between our two Hydrophobic acid points is 1, AND these points aren't sequential in the sequence, we have a topological neighbor.
                if (curr.distance(next) == 1 && (Math.abs(nextIndex - currIndex)  != 1)) {
                    fitnessBonds.add(curr);
                    fitnessBonds.add(next);
                    fitness++;
                }
            }

            s.setFitness(new Fitness(fitness, fitnessBonds));
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

    private static void mutate(Structure structure) {
    }

    private static Structure crossOver(Structure structure1, Structure structure2) {
        Structure s = new Structure();
        boolean structureValid = false;
        while (!structureValid) {
            int randomPosition = new Random().nextInt(structure1.getNodes().size());

            List<StructureNode> nodes = new ArrayList<>();
            for (int i = 0; i < randomPosition; i++) {
                nodes.add(structure1.getNodes().get(i));
            }
            for (int i = randomPosition; i < structure2.getNodes().size(); i++) {
                nodes.add(structure2.getNodes().get(i));
            }

            List<Point> visited = new ArrayList<>();
            for (StructureNode n : nodes) {
                visited.add(n.getPosition());
            }
            s = new Structure(structure1.getSequence(), new Fitness(0, new HashSet<Point>()), nodes, visited);
            structureValid = validateStructure(s);
        }

        logger.debug("WE MADE IT!!!");

        return s;
    }

    private static boolean validateStructure(Structure structure) {
        List<StructureNode> nodes = structure.getNodes();
        for (int i = 0; i < nodes.size()-1; i++) {
            if (nodes.get(i).getPosition().distance(nodes.get(i+1).getPosition()) != 1) {
                return false;
            }
        }

        return true;
    }

}
