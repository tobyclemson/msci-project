package ic.msciproject.minoritygame;

import java.util.Random;

/**
 * The HistoryString class encapsulates the concept of the global history string
 * in the minority game. The history string has a fixed length and holds a
 * string of "0"s and "1"s representing the outcomes of previous time steps in
 * the minority game.
 * @author tobyclemson
 */
public class HistoryString {
    
    /**
     * An integer holding the length of the history string. This is returned by
     * the {@link #getLength} method.
     */
    private int length;

    /**
     * A StringBuffer representing this history string. The array is
     * returned in string form by the {@link #toString} method
     */
    private StringBuilder contents;

    /**
     * Constructs a HistoryString instance of the supplied length
     * @param length The length of the history string.
     */
    public HistoryString(int length){
        Random numberGenerator = new Random();
        this.length = length;
        this.contents = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            contents.append(numberGenerator.nextInt(2));
        }
    }

    /**
     * Returns the length of this history string as set at initialisation as an
     * integer.
     * @return The length of this history string as an integer.
     */
    public int getLength(){
        return length;
    }

    /**
     * Converts the object into its string representation (a string of the same
     * length as the length specified at creation of '0's and '1's.
     * @return The string representation of the object.
     */
    @Override
    public String toString() {
        return contents.toString();
    }

    public void push(String stringToPush) {
        contents.deleteCharAt(0);
        contents.append(stringToPush);
    }
}
