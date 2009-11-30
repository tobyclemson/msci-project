package ic.msciproject;

import ic.msciproject.minoritygame.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Map;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JMenuBar;
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;

/**
 *
 * @author Toby Clemson
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        //javax.swing.SwingUtilities.invokeLater(new Runnable() {
        //    public void run() {
        //        createAndShowGUI();
        //    }
        //});

        // declare required variables
        Map<Choice, Integer> attendance;
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
        properties.setProperty("agent-memory-size", "5");

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
            output.append(time + "," + attendance.get(Choice.A) + "\n");
            if(time % 100 == 0) {
                System.out.printf("Time: %d\n", time);
            }
        }

        // write the data to file
        outputFile.write(output.toString());

        // make sure everything is flushed and close the file
        outputFile.close();
    }

    public static void createAndShowGUI() {
        // create and set up the window.
        //JFrame frame = new JFrame("Minority Game Simulator");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // display the window.
        //frame.pack();
        //frame.setVisible(true);
    }
}
