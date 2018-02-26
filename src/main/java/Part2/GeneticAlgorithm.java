package Part2;

import javafx.application.Application;
import javafx.stage.Stage;
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
    private static final float CROSSOVER_RATE = 0.85f;
    private static final float MUTATION_RATE = 0.025f;
    private static final float ELITE_RATE = 0.075f;

    public static void main(String[] args) {
        initPopulation();
    }
    //1. initialize population -- DONE

    //2. compute fitness of population 1 for all chromosomes -- i think it's done.

    //3. bubbleSortPopulation -- DONE

    //4. exit condition: does fitness = -42? if yes, you're done

    //5. elite: choose 10 best chromosomes

    //6. crossover: do 80x to produce 160 chromosomes

    //7. fill the rest of population 2 randomly

    //8. mutation: apply to 10 chromosomes

    //9. pop1 = pop2
    //   gen++

    //10. go to step 2.


    /**
     * This method will initialize population based on selected input sequence.
     */
    public static Structure initPopulation() {
        ProteinParser parser = new ProteinParser(new File("src/main/resources/Input.txt"));
        List<Protein> proteins = parser.getProteins();

        //Just using the 4th sequence for now.
        Protein p = proteins.get(4);
        logger.debug("Initial protein input");
        logger.debug("Sequence = " + p.getFormattedSequenceString(p.getSequence()) + " | Fitness = -" + p.getFitness());
        List<Structure> structures = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            Structure s = selfAvoidingWalk(p);
            structures.add(s);
        }
        population = new Population(structures);
        computeFitness(population, p.getFormattedSequenceString(p.getSequence()));
        //this Java 8 lambda expression is a one-liner to sort the population by fitness value.
        population.getStructures().sort(Comparator.comparing(Structure::getFitness));


        //then, perform the elitism function. (take the last 10 structures bc they have the highest fitness)
        //do crossover & mutation
        //the resulting structures will give you a new population

        return population.getStructures().get(population.getStructures().size()-1);
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
            logger.debug("Move number = " + i);
            logger.debug("Sequence size = " + protein.getSequence().size());
            logger.debug("Node size = " + nodes.size());
            randomOrientation(i, protein.getSequence().get(i).getLetter(), nodes, visited);
        }

        logger.info("Moves: ");
        for (StructureNode n : nodes) {
            logger.info(n.getAminoAcid() + " (" + n.getPosition().getX() + "," + n.getPosition().getY() + ")");
        }

        return new Structure(protein.getFormattedSequenceString(protein.getSequence()), 0, nodes, visited);
    }

    private static void randomOrientation(int moveNum, String acid, List<StructureNode> nodes, List<Point> visited) {
        //check prev position
        Point curr = nodes.get(nodes.size()-1).getPosition();

        //all adjacent points to the previous point.
        List<Point> possibleMoves = new ArrayList<>();
        Point nextRight = addPoints(curr, Direction.RIGHT.getPoint());
        Point nextLeft = addPoints(curr, Direction.LEFT.getPoint());
        Point nextUp = addPoints(curr, Direction.UP.getPoint());
        Point nextDown = addPoints(curr, Direction.DOWN.getPoint());
        if (!visited.contains(nextRight)) { possibleMoves.add(nextRight); }
        if (!visited.contains(nextLeft)) { possibleMoves.add(nextLeft); }
        if (!visited.contains(nextUp)) { possibleMoves.add(nextUp); }
        if (!visited.contains(nextDown)) { possibleMoves.add(nextDown); }


        while (possibleMoves.size() == 0) {
            //keep trying until you find a possible move. this means backtracking to the previous point and erasing the current. choose a random point from the previous point again,
            //as long as it's not the same random point.
            //make sure to update current point/ if node size changes, then curr position also needs to be updated.
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
    private static void computeFitness(Population population, String sequence) {
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
            List<Pair<Point, Point>> fitnessBonds = new ArrayList<>();
            for (int i = 0; i < hydrophobics.size()-1; i++) {
                logger.info("("+hydrophobics.get(i).getPosition().getX()+","+hydrophobics.get(i).getPosition().getY()+")");
                Point curr = hydrophobics.get(i).getPosition();
                int currIndex = s.getNodes().indexOf(new StructureNode("h", curr));
                Point next = hydrophobics.get(i+1).getPosition();
                int nextIndex = s.getNodes().indexOf(new StructureNode("h", next));
                if (curr.distance(next) == 1 && (Math.abs(nextIndex - currIndex)  != 1)) {
                    fitnessBonds.add(new Pair<>(curr, next));
                    fitness++;
                }
            }

            s.setFitness(fitness);
            s.setFitnessBond(fitnessBonds);
        }
    }

    /**
     * Simple method to generate two new sequences from crossover operation on two input sequences.
     * @param sequence1 1st sequence string
     * @param sequence2 2nd sequence string
     */
    private static void crossOver(String sequence1, String sequence2) {
        logger.info("Crossover operation results");
        logger.info("---------------------------");
        logger.info("Input sequence 1 = " + sequence1);
        logger.info("Input sequence 2 = " + sequence2);
        //choose a random position in the string as the crossover point
        int crossOverPoint = new Random().nextInt(sequence1.length());

        String seq1FirstHalf = sequence1.substring(0, crossOverPoint);
        String seq1SecondHalf = sequence1.substring(crossOverPoint, sequence1.length());
        String seq2FirstHalf = sequence2.substring(0, crossOverPoint);
        String seq2SecondHalf = sequence2.substring(crossOverPoint, sequence2.length());

        //build crossover strings based on two halves of the sequence strings
        String crossover1 = new StringBuilder().append(seq1FirstHalf).append(seq2SecondHalf).toString();
        String crossover2 = new StringBuilder().append(seq2FirstHalf).append(seq1SecondHalf).toString();

        logger.info("crossover 1 = " + crossover1);
        logger.info("crossover 2 = " + crossover2);
    }

    /**
     * Simple method to generate a new sequence from mutation/bit-flip operation on input sequence.
     * @param sequence The sequence we want to mutate.
     * @return new sequence string with one "bit"/acid flipped from h->p or p->h
     */
    private static String mutation(String sequence) {
        logger.info("Mutation operation results");
        logger.info("--------------------------");
        logger.info("Input sequence = " + sequence);
        char[] acids = sequence.toCharArray();
        //choose random index from string to flip bit
        int index = new Random().nextInt(sequence.length());
        //make a stringbuilder from sequence string
        StringBuilder sb = new StringBuilder(sequence);
        //bit flip operation
        char c = (acids[index]=='h') ? ('p') : ('h');
        //changes the character at the random index
        sb.setCharAt(index, c);
        logger.info("Mutation result = " + sb.toString());

        return sb.toString();
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
}
