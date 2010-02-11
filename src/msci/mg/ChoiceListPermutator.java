package msci.mg;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * The ChoiceListPermutator class allows permutations of choices to be generated
 * as a Set of Lists of Choice enumerations.
 * @author tobyclemson
 */
public class ChoiceListPermutator {

    /**
     * A Map of integers to sets of all possible Choice permutations for that
     * integer. This is used to memoize permutations to avoid repeated
     * generation of equivalent data.
     */
    private static Map<Integer, Set<List<Choice>>> storedPermutationSets =
        new HashMap<Integer, Set<List<Choice>>>();

    /**
     * Generates the set of all permutations of Choice.A and Choice.B of the
     * specified length.
     * @param permutationLength The length of permutations required.
     * @return A Set containing all possible permutations of the specified
     * length.
     */
    public static Set<List<Choice>> generateAll(int permutationLength) {
        // if a set of permutations of the specified length has already been
        // generated, return it, otherwise generate the set, store it and 
        // return it.
        if(storedPermutationSets.containsKey(permutationLength)) {
            return storedPermutationSets.get(permutationLength);
        } else {
            // declare all required variables.
            Set<List<Choice>> permutations;
            int integerToConvert, positionInList, integerCopy;
            List<Choice> currentPermutation;

            // create an empty HashSet to hold the permutations.
            permutations = new HashSet<List<Choice>>();

            // for each integer between zero and the number of possible 
            // permutations minus one, convert the number into its binary string
            // representation replacing '0' with Choice.A and '1' with Choice.B
            // and add it to the set of permutations.
            for(
                integerToConvert = 0;
                integerToConvert < Math.pow(2, permutationLength);
                integerToConvert++
            ) {
                // create an ArrayList to hold the permutation whilst it is
                // being constructed.
                currentPermutation = new ArrayList<Choice>(permutationLength);

                // create a copy of the integer to be converted so that it can 
                // be modified.
                integerCopy = integerToConvert;

                // convert the integer to its binary representation with '0'
                // replaced by Choice.A and '1' by Choice.B.
                for(
                    positionInList = 0;
                    positionInList < permutationLength;
                    positionInList++
                ) {
                    switch(integerCopy % 2) {
                        case 0:
                            currentPermutation.add(Choice.A);
                            break;
                        case 1:
                            currentPermutation.add(Choice.B);
                            break;
                    }
                    integerCopy /= 2;
                }

                // add the permutation to the set of permutations.
                permutations.add(currentPermutation);
            }

            // add the permutation set to the stored map of permutation sets
            // keyed by key length
            storedPermutationSets.put(permutationLength, permutations);

            // return the set of permutations.
            return permutations;
        }
    }
}
