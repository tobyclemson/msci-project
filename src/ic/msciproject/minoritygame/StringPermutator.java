package ic.msciproject.minoritygame;

import java.util.HashSet;

public class StringPermutator {
    public static HashSet<String> generateAll(int stringLength) {
        HashSet<String> permutations = new HashSet<String>();
        for(int i = 0; i < Math.pow(2, stringLength); i++) {
            StringBuilder currentPermutation = new StringBuilder(stringLength);
            int k = i;
            for(int j = 0; j < stringLength; j++) {
                currentPermutation.insert(0, k % 2);
                k /= 2;
            }
            permutations.add(currentPermutation.toString());
        }
        return permutations;
    }
}
