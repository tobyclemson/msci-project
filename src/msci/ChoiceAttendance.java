package msci;

import msci.mg.*;
import msci.mg.factories.MinorityGameFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ChoiceAttendance {
    public static void main(String[] args) throws IOException {
        StringBuilder output;
        Properties properties;
        FileWriter outputFile;
        MinorityGame minorityGame;

        properties = new Properties();
        properties.setProperty("number-of-agents", "1001");
        properties.setProperty("agent-type", "basic");
        properties.setProperty("agent-memory-size", "5");

        int numberOfRuns = 100;
        int numberOfTimeSteps = 22800;

        for(int s = 2; s <= 4; s++) {
            properties.setProperty("number-of-strategies-per-agent", new Integer(s).toString());

            output = new StringBuilder();

            int data[][] = new int[numberOfRuns][numberOfTimeSteps];

            System.out.println("Starting for s=" + s);

            for(int run = 0; run < numberOfRuns; run++) {
                System.out.println("Executing run " + run);

                minorityGame = MinorityGameFactory.construct(properties);

                int attendanceOfA;

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
                        String column;
                        if(i < 26) {
                            column = String.valueOf((char) (65 + i));
                        } else {
                            column = String.valueOf((char) (64 + i/26)) +
                                String.valueOf((char) (65 + i - 26 * (i/26)));
                        }

                        output.append("=stdev(")
                                .append(column).append("1:")
                                .append(column).append(numberOfTimeSteps)
                                .append(")");
                    } else {
                        output.append(data[i][j]);
                    }
                }
                output.append("\n");
            }

            outputFile = new FileWriter(
                "/Users/tobyclemson/Documents/Degree Work/4th Year/MSci " +
                "Project/Results/choice-A-attendance-S=" + s + ".csv");

            outputFile.write(output.toString());
            outputFile.close();
        }
    }
}
