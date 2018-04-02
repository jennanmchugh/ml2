package Part1;

import com.google.common.base.CharMatcher;

import java.util.*;
/**
  *  Created on 02/20/18 by Jenna McHugh.
 **/
public class HillClimber {
    //set in program instructions
    private static final int MAX = 100;
    private static boolean local;

    public static void main (String[] args) {
        int t = 0;
        int[] maxValues = new int[100];
        while (t!=MAX) {
            local = false;
            String vc = buildRandomBitString(40);
            int fVc = getFunctionValue(vc);
            while (!local) {
                List<String> neighborStrings = buildNeighborStrings(vc);
                HashMap<String, Integer> neighborValuePair = new HashMap<>();
                for (String s : neighborStrings) {
                    neighborValuePair.put(s, getFunctionValue(s));
                }
                Map.Entry<String, Integer> maxEntry = null;
                for (Map.Entry<String, Integer> entry : neighborValuePair.entrySet()) {
                    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                        maxEntry = entry;
                    }
                }
                String vn = maxEntry.getKey();
                int fVn = getFunctionValue(vn);
                maxValues[t] = fVn;
                if (fVc < fVn) {
                    vc = vn;
                    fVc = getFunctionValue(vc);
                }
                else {
                    local = true;
                }
            }
            t++;
        }
        for (int i = 0; i < maxValues.length; i++) {
            if (i < maxValues.length-1) {
                System.out.print(maxValues[i] + ",");
            }
            else {
                System.out.println(maxValues[i]);
            }
        }
    }

    //makes use of Google Guava to return num of 1's in a string.
    private static int getNumOfOnesInString(String input) {
        return CharMatcher.is('1').countIn(input);
    }

    /**
     * Builds a bit string of length numOfBits composed of random bits.
     * @param numOfBits the desired length of the bit string
     * @return the bit string
     */
    private static String buildRandomBitString(int numOfBits) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < numOfBits; i++) {
            Random random = new Random();
            //this will return either 0 or 1.
            int randomNumber = random.nextInt(2);
            sb.append(randomNumber);
        }

        return sb.toString();
    }

    /**
     * Plugs bit string into the function and calculates function value.
     * @param input bit string
     * @return function value
     */
    private static int getFunctionValue(String input) {
        return Math.abs(12 * getNumOfOnesInString(input) - 160);
    }

    /**
     * Creates neighbor strings from input string by flipping one bit at a time. The length of the input string = the number of neighbor strings returned.
     * @param vc current bit string
     * @return
     */
    private static List<String> buildNeighborStrings(String vc) {
        List<String> neighborStrings = new ArrayList<>();
        char[] bits = vc.toCharArray();
        for (int i = 0; i < bits.length; i++) {
            StringBuilder sb = new StringBuilder(vc);
            char c = (bits[i]=='1') ? ('0') : ('1');
            sb.setCharAt(i, c);
            neighborStrings.add(sb.toString());
        }

//        System.out.println("String vc is currently = " + vc);
//        System.out.println("40 neighbor strings for vc =");
//        for (String s : neighborStrings) {
//            System.out.println(s);
//        }

        return neighborStrings;
    }
}