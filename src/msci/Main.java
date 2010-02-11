package msci;

import msci.mg.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
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
        StringBuilder output;
        Properties properties;
        FileWriter outputFile;
        MinorityGame minorityGame;

        // initialise game properties
        properties = new Properties();
        properties.setProperty("type", "standard");
        properties.setProperty("number-of-agents", "101");
        properties.setProperty("agent-type", "basic");
        properties.setProperty("number-of-strategies-per-agent", "2");

        // set up the run harness
        int numberOfRuns = 32;
        int numberOfTimeSteps = 10000;

        // execute the simulation for values of m between 2 and 16
        for(int m = 2; m <= 16; m++) {
            properties.setProperty(
                "agent-memory-size", new Integer(m).toString()
            );

            // create a string builder to hold the data
            output = new StringBuilder();

            // create a 2D array to hold the resulting data
            int data[][] = new int[numberOfRuns][numberOfTimeSteps];

            System.out.println("Starting for m=" + m);

            // run the game the required number of times
            for(int run = 0; run < numberOfRuns; run++) {

                System.out.println("Executing run " + run);

                // construct a game using the chosen properties
                minorityGame = MinorityGameFactory.construct(properties);

                // create a variable to store the attendance of choice A at each
                // time step
                int attendanceOfA = 0;

                // run the game
                for(int time = 0; time < numberOfTimeSteps; time++) {
                    // step forward in time
                    minorityGame.stepForward();

                    // get the attendance for choice A
                    attendanceOfA = minorityGame.
                        getCommunity().
                        getChoiceTotals().
                        get(Choice.A);

                    // set the relevant data point to the attendance
                    data[run][time] = attendanceOfA;
                }
            }

            // output the results to file
            for(int j = 0; j <= numberOfTimeSteps; j++) {
                for(int i = 0; i < numberOfRuns; i++) {
                    if(i != 0) {
                        output.append(",");
                    }
                    if (j == numberOfTimeSteps) {
                        String column = null;
                        if(i < 26) {
                            column = String.valueOf((char) (65 + i));
                        } else {
                            column = String.valueOf((char) (64 + i/26)) +
                                String.valueOf((char) (65 + i - 26 * (i/26)));
                        }

                        output.append(
                            "=stdev(" +
                            column +
                            "1:" +
                            column +
                            numberOfTimeSteps +
                            ")"
                        );
                    } else {
                        output.append(data[i][j]);
                    }
                }
                output.append("\n");
            }

            // open a file to write the data
            outputFile = new FileWriter(
                "/Users/tobyclemson/Desktop/sigma-" + m + ".csv"
            );

            // write the data to file
            outputFile.write(output.toString());

            // make sure everything is flushed and close the file
            outputFile.close();
        }
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
