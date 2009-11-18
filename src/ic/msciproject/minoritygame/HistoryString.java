package ic.msciproject.minoritygame;

public class HistoryString {
    private int length;

    public HistoryString(){
        this.length = 1;
    }

    public HistoryString(int length){
        this.length = length;
    }

    public int getLength(){
        return length;
    }

    public void setLength(int i){
        this.length = i;
    }
}
