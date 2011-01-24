package msci.mg;

import java.util.*;

public class ChoiceListPermutator {
    private static Map<Integer, Set<List<Choice>>> storedPermutationSets = new HashMap<Integer, Set<List<Choice>>>();

    public static Set<List<Choice>> generateAll(int permutationLength) {
        if (storedPermutationSets.containsKey(permutationLength)) {
            return storedPermutationSets.get(permutationLength);
        } else {
            Set<List<Choice>> permutations = new HashSet<List<Choice>>();

            for (int integerToConvert = 0; integerToConvert < Math.pow(2, permutationLength); integerToConvert++) {
                List<Choice> currentPermutation = new ArrayList<Choice>(permutationLength);

                int integerCopy = integerToConvert;

                for (int positionInList = 0; positionInList < permutationLength; positionInList++) {
                    switch (integerCopy % 2) {
                        case 0:
                            currentPermutation.add(Choice.A);
                            break;
                        case 1:
                            currentPermutation.add(Choice.B);
                            break;
                    }
                    integerCopy /= 2;
                }

                permutations.add(currentPermutation);
            }

            storedPermutationSets.put(permutationLength, permutations);

            return permutations;
        }
    }
}
