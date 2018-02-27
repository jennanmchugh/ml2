package Part2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 02/20/18 by Jenna McHugh.
 * Simple class that parses the input text file and generates a list of proteins & their maximum possible fitness values from the file. Also uses logging to show the list.
 */
public class ProteinReader {

    private static final Logger logger = LoggerFactory.getLogger(ProteinReader.class);
    private List<Protein> proteins = new ArrayList<>();

    ProteinReader(File path) {

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                if (sCurrentLine.startsWith("Seq = ")) {
                    List<AminoAcid> aminos = new ArrayList<>();
                    String sequence = sCurrentLine.substring(6, sCurrentLine.length());
                    for (int i =0; i < sequence.length(); i++) {
                        if (sequence.charAt(i) == 'h') {
                            //hydrophobic acid
                            aminos.add(new AminoAcid(String.valueOf(sequence.charAt(i)), true));
                        }
                        else if (sequence.charAt(i) == 'p') {
                            //hydrophilic acid
                            aminos.add(new AminoAcid(String.valueOf(sequence.charAt(i)), false));
                        }
                    }
                    //get the next line which contains the sequence's fitness value
                    sCurrentLine = br.readLine();
                    int fitness = Integer.valueOf(sCurrentLine.substring(11, sCurrentLine.length()));
                    Protein protein = new Protein(aminos, fitness);
                    proteins.add(protein);
                }
            }

        } catch (IOException e) {
            logger.error("Something went wrong reading the input file... " , e);
        }

        printProteins(proteins);

    }

    /**
     * A simple method that will log protein information from input file.
     * @param proteins the list of proteins generated from the input file
     */
    private static void printProteins(List<Protein> proteins) {
        int i = 1;
        for (Protein p : proteins) {
            logger.info("" + i +".) ");
            logger.info("Sequence = " + p.getFormattedSequenceString(p.getSequence()) + " | Fitness = " + p.getFitness());
            logger.info("");
            i++;
        }
    }

    public List<Protein> getProteins() { return proteins; }
}
