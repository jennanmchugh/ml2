package Part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 02/20/18 by Jenna McHugh.
 */
public class GeneticAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(GeneticAlgorithm.class);
    private static final float CROSSOVER_RATE = 0.85f;
    private static final float MUTATION_RATE = 0.025f;
    private static final float ELITE_RATE = 0.075f;

    public static void main(String[] args) {
        initPopulation();
    }
    //1. initialize population

    //2. compute fitness of population 1 for all chromosomes

    //3. bubbleSortPopulation

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
    private static void initPopulation() {
        ProteinParser parser = new ProteinParser(new File("src/main/resources/Input.txt"));
        List<Protein> proteins = parser.getProteins();

        //use the 4th protein as example to test
        Protein p = proteins.get(4);
        logger.debug("Initial protein input");
        logger.debug("Sequence = " + p.getFormattedSequenceString(p.getSequence()) + " | Fitness = " + p.getFitness());
        selfAvoidingWalk(p);
    }

    /**
     * This method implements the self avoiding walk. It always chooses (0,0) as position 1, and (1,0) as position 2. From then on, the next moves are
     * chosen randomly based on available directions. Fails/ structure deemed invalid if there are no more available moves.
     * @param protein the sequence we are drawing a structure from.
     */
    private static void selfAvoidingWalk(Protein protein) {
        HashMap<String, Point> graph = new HashMap<>();
        Set<Point> coords = new HashSet<>();

        //we always start at the origin (0,0)
        coords.add(new Point(0, 0));
        graph.put(protein.getSequence().get(0).getLetter(), new Point(0, 0));
        //we always place the second char at (1,0)
        coords.add(new Point(1, 0));
        graph.put(protein.getSequence().get(1).getLetter(), new Point(1, 0));

        //start i counter at 2 since we have already set the values for positions 0 & 1.
        for (int i = 2; i < protein.getSequence().size(); i++) {

        }

        //now we self avoid... meaning our choice of direction for Step #3 is either Direction.UP, Direction.RIGHT, Direction.DOWN...


    }

    /**
     * Computes fitness value of structure. This number is also known as "topological neighbors" (TN): the number of neighboring H-H contacts where the
     * H's are not already covalently bonded or sequentially connected within the sequence.
     * @return fitness value.
     */
    private int computeFitness() {
        return 0;
    }

    /**
     * This method will use the bubble sort algorithm (modified from www.javatpoint.com/bubble-sort-in-java) to sort the population by fitness.
     * @param proteins the population we are sorting.
     */
    private static void bubbleSortPopulation(List<Protein> proteins) {
        int n = proteins.size();
        int temp;
        Protein[] pArray = (Protein[])proteins.toArray();
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < (n - i); j++) {
                if (pArray[j - 1].getFitness() > pArray[j].getFitness()) {
                    //swap elements
                    temp = pArray[j - 1].getFitness();
                    pArray[j - 1] = pArray[j];
                    pArray[j].setFitness(temp);
                }

            }
        }
    }
}
