package msci;

import msci.mg.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import msci.mg.factories.MinorityGameFactory;

public class Main {

    public static void main(String[] args) throws IOException {
        StringBuilder output;
        Properties properties;
        FileWriter outputFile;
        MinorityGame minorityGame;

        properties = new Properties();
        properties.setProperty("number-of-agents", "101");
        properties.setProperty("agent-type", "basic");
        properties.setProperty("number-of-strategies-per-agent", "2");

        int numberOfRuns = 32;
        int numberOfTimeSteps = 10000;

        for(int m = 2; m <= 16; m++) {
            properties.setProperty(
                "agent-memory-size", new Integer(m).toString()
            );

            output = new StringBuilder();

            int data[][] = new int[numberOfRuns][numberOfTimeSteps];

            System.out.println("Starting for m=" + m);

            for(int run = 0; run < numberOfRuns; run++) {

                System.out.println("Executing run " + run);

                minorityGame = MinorityGameFactory.construct(properties);

                int attendanceOfA = 0;

                for(int time = 0; time < numberOfTimeSteps; time++) {
                    minorityGame.stepForward();

                    attendanceOfA = minorityGame.
                        getCommunity().
                        getChoiceTotals().
                        get(Choice.A);

                    data[run][time] = attendanceOfA;
                }
            }

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

            outputFile = new FileWriter(
                "/Users/tobyclemson/Desktop/sigma-" + m + ".csv"
            );

            outputFile.write(output.toString());

            outputFile.close();
        }
    }
}
