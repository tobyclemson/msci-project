package ic.msciproject.minoritygame;

import java.util.Set;
import java.util.HashSet;
/**
 * The StringPermutator class allows permutations of strings consisting of "0"s
 * and "1"s to be generated.
 * @author tobyclemson
 */
public class StringPermutator {

    /**
     * Generates the set of all permutations of strings of "0"s and "1"s or the
     * specified string length.
     * @param stringLength The length of strings required.
     * @return A Set containing all possible permutations of the specified
     * length.
     */
    public static Set<String> generateAll(int stringLength) {
        // declare all required variables.
        HashSet<String> permutations;
        StringBuilder currentPermutation;
        int integerToConvert, positionInString, integerCopy;

        // create an empty HashSet to hold the permutations.
        permutations = new HashSet<String>();

        // for each integer between zero and the number of possible permutations
        // minus one, convert the number into its binary string representation
        // and add it to the set of permutations.
        for(
            integerToConvert = 0;
            integerToConvert < Math.pow(2, stringLength);
            integerToConvert++
        ) {
            // create a string builder to hold the permutation whilst it is
            // being constructed.
            currentPermutation = new StringBuilder(stringLength);

            // create a copy of the integer to be converted so that it can be
            // modified.
            integerCopy = integerToConvert;

            // convert the integer to a binary string.
            for(
                positionInString = 0;
                positionInString < stringLength;
                positionInString++
            ) {
                currentPermutation.insert(0, integerCopy % 2);
                integerCopy /= 2;
            }

            // add the string to the set of permutations.
            permutations.add(currentPermutation.toString());
        }

        // return the set of permutations.
        return permutations;
    }
}
