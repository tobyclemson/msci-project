package ic.msciproject.minoritygame;

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
     * Constructs a HistoryString instance of the supplied length
     * @param length The length of the history string.
     */
    public HistoryString(int length){
        this.length = length;
    }

    /**
     * Returns the length of this history string as set at initialisation as an
     * integer.
     * @return The length of this history string as an integer.
     */
    public int getLength(){
        return length;
    }
}
