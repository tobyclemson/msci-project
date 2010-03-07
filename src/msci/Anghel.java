package msci;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import msci.mg.Friendship;
import msci.mg.MinorityGame;
import msci.mg.Agent;
import msci.mg.agents.NetworkedAgent;
import msci.mg.factories.MinorityGameFactory;

/**
 * Calculates the in degree distribution for a number of different parameter
 * sets and outputs them to file.
 * @author tobyclemson
 */
public class Anghel {
    public static void main(String[] args) throws IOException {
        // declare required variables
        Properties properties;
        MinorityGame minorityGame;

        StringBuilder output;
        FileWriter outputFile;

        DirectedGraph<Agent, Friendship> leadershipStructure;

        int numberOfTimeSteps,
            numberOfRuns,
            maximumDegree,
            numberOfRows,
            numberOfColumns;
        
        double[][] resultsTable;

        String[][] runProperties = {
            {"201", "0.1"},
            {"201", "0.2"},
            {"601", "0.1"},
            {"601", "0.2"},
            {"1001", "0.1"}
        };

        // set the required number of timesteps and runs and the maximum
        // degree
        numberOfTimeSteps = 100000;
        numberOfRuns = 20;
        maximumDegree = 200;
        numberOfRows = maximumDegree + 2;
        numberOfColumns = numberOfRuns + 1;

        // iterate through all property sets
        for(
            int propertySet = 0;
            propertySet < runProperties.length;
            propertySet++
        ) {
            // initialise game properties
            properties = new Properties();
            properties.setProperty(
                "number-of-agents", runProperties[propertySet][0]
            );
            properties.setProperty("agent-type", "networked");
            properties.setProperty("number-of-strategies-per-agent", "2");
            properties.setProperty("network-type", "random");
            properties.setProperty(
                "link-probability", runProperties[propertySet][1]
            );
            properties.setProperty("agent-memory-size", "6");

            // initialise a StringBuilder to store the output
            output = new StringBuilder();

            // initialise the results table
            resultsTable = new double[numberOfRows][numberOfColumns];

            // set the column headings
            for(int i = 0; i < numberOfColumns; i++) {
                if(i == 0) {
                    continue;
                }
                resultsTable[0][i] = i;
            }

            // set the row labels
            for(int i = 0; i < numberOfRows; i++) {
                if(i == 0) {
                    continue;
                }
                resultsTable[i][0] = i-1;
            }

            // do the required number of runs
            for(int run = 1; run <= numberOfRuns; run++) {
                // construct a game using the chosen properties
                minorityGame = MinorityGameFactory.construct(properties);

                System.out.println("Starting run: " + run);

                // run for the number of timesteps required for the results to
                // stabilise
                for(int timeStep = 0; timeStep < numberOfTimeSteps; timeStep++) {
                    if(timeStep % 1000 == 0){
                        System.out.println(timeStep);
                    }
                    minorityGame.stepForward();
                }
                
                int measurementCount = 0;

                // average over 1000 leadership structures for this graph and
                // strategy space
                for(int timeStep = 0; timeStep < numberOfTimeSteps; timeStep++) {
                    if(timeStep % 1000 == 0) {
                        System.out.println(numberOfTimeSteps + timeStep);
                    }

                    // make a measurement every 100 timesteps
                    if(timeStep % 100 == 0){
                        // calculate the in-degree distribution

                        // create a directed graph to hold the leadership tree
                        leadershipStructure =
                            new DirectedSparseGraph<Agent, Friendship>();

                        // add all the agents to the graph
                        for(Agent agent : minorityGame.getAgents()) {
                            leadershipStructure.addVertex(agent);
                        }

                        // add all the valid  edges to the graph
                        for(Agent agent : minorityGame.getAgents()) {
                            if(!(agent instanceof NetworkedAgent)) {
                                continue;
                            }

                            NetworkedAgent networkedAgent =
                                (NetworkedAgent) agent;

                            // prevent self loops
                            if(networkedAgent.getBestFriend() == agent) {
                                continue;
                            }
                            // create a directed edge
                            leadershipStructure.addEdge(
                                new Friendship(),
                                networkedAgent,
                                networkedAgent.getBestFriend(),
                                EdgeType.DIRECTED
                            );
                        }

                        // populate the row for this run in the results table with the
                        // in-degree distribution
                        for(Agent agent : leadershipStructure.getVertices()) {
                            int inDegree = leadershipStructure.inDegree(agent);
                            resultsTable[inDegree + 1][run] += 1;
                        }

                        measurementCount += 1;
                    }
                    minorityGame.stepForward();
                }

                // normalise
                for(int inDegreeIndex = 1; inDegreeIndex < numberOfRows; inDegreeIndex++) {
                    resultsTable[inDegreeIndex][run] /= (double) measurementCount;
                }
            }

            // convert the results table to a csv string
            for(int i = 0; i < numberOfRows; i++) {
                for(int j = 0; j < numberOfColumns; j++) {
                    if(j != 0) {
                        output.append(",");
                    }
                    if(i == 0 && j == 0) {
                        continue;
                    } else {
                        output.append(resultsTable[i][j]);
                    }
                }
                output.append("\n");
            }

            // open a file to write the data to
            outputFile = new FileWriter(
                "/Users/tobyclemson/Documents/Degree Work/4th Year/MSci Project" +
                "/Results/Anghel Game/in-degree-distribution-N=" +
                properties.getProperty("number-of-agents") + "-p=" +
                properties.getProperty("link-probability") + "-m=6-S=2-" +
                "t=100000.csv"
            );

            // write the results string to the file
            outputFile.write(output.toString());

            // close the file
            outputFile.close();
        }
    }
}
