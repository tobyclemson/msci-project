package ic.msciproject;

import ic.msciproject.minoritygame.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Map;

/**
 *
 * @author Toby Clemson
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // declare required variables
        Map<String, Integer> attendance;
        StringBuilder output;
        Properties properties;
        FileWriter outputFile;
        AbstractMinorityGame minorityGame;

        // initialise game properties
        properties = new Properties();
        properties.setProperty("type", "standard");
        properties.setProperty("number-of-agents", "1001");
        properties.setProperty("agent-type", "basic");
        properties.setProperty("number-of-strategies-per-agent", "5");
        properties.setProperty("history-string-length", "10");
        
        // construct a game using the chosen properties
        minorityGame = MinorityGameFactory.construct(properties);
        
        // open a file to write the data
        outputFile = new FileWriter(
            "/Users/tobyclemson/Desktop/attendance-vs-time-m=10.csv"
        );
        
        // create a string builder to hold the data
        output = new StringBuilder();
        
        // run the game 
        for(int time = 0; time <= 1000; time++) {
            minorityGame.stepForward();
            attendance = minorityGame.getAgents().getLastChoiceTotals();
            output.append(time + "," + attendance.get("0") + "\n");
            System.out.printf("Time: %d\n", time);
        }

        // write the data to file
        outputFile.write(output.toString());

        // make sure everything is flushed and close the file
        outputFile.close();
    }

}
