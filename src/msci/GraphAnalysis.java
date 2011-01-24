package msci;

import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import hep.aida.bin.StaticBin1D;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import msci.mg.agents.Agent;
import msci.mg.Friendship;
import msci.mg.factories.AgentFactory;
import msci.mg.factories.FriendshipFactory;
import msci.mg.factories.RandomAgentFactory;
import msci.mg.factories.RandomSocialNetworkFactory;
import org.apache.commons.collections15.Transformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class GraphAnalysis {
    public static void main(String[] args) throws IOException {
        int[] numberOfAgents = {201, 201, 601, 601, 1001};
        double[] linkProbability = {0.1, 0.2, 0.1, 0.2, 0.1};

        int numberOfGraphsToAverage = 20;
        int numberOfPropertySets = numberOfAgents.length;

        AgentFactory agentFactory = new RandomAgentFactory();
        FriendshipFactory friendshipFactory = new FriendshipFactory();

        RandomSocialNetworkFactory graphFactory;
        Graph<Agent, Friendship> graph;

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Graph Analysis Data");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(2).setCellValue("Average Degree");
        headerRow.createCell(3).setCellValue("Minimum Degree");
        headerRow.createCell(4).setCellValue("Maximum Degree");
        headerRow.createCell(5).setCellValue("Diameter");
        headerRow.createCell(6).setCellValue("Average Distance");
        headerRow.createCell(7).setCellValue("Average Clustering Coefficient");

        for(int i = 0; i < numberOfPropertySets; i++) {
            int currentNumberOfAgents = numberOfAgents[i];
            double currentLinkProbability = linkProbability[i];

            graphFactory = new RandomSocialNetworkFactory(
                agentFactory,
                friendshipFactory,
                currentNumberOfAgents,
                currentLinkProbability);

            StaticBin1D averageDegrees = new StaticBin1D();
            StaticBin1D minimumDegrees = new StaticBin1D();
            StaticBin1D maximumDegrees = new StaticBin1D();
            StaticBin1D diameters = new StaticBin1D();
            StaticBin1D averageDistances = new StaticBin1D();
            StaticBin1D averageClusteringCoefficients = new StaticBin1D();

            for(int j = 0; j < numberOfGraphsToAverage; j++) {
                graph = graphFactory.create();

                averageDegrees.add(getAverageDegree(graph));
                minimumDegrees.add(getMinimumDegree(graph));
                maximumDegrees.add(getMaximumDegree(graph));
                diameters.add(getDiameter(graph));
                averageDistances.add(getAverageDistance(graph));
                averageClusteringCoefficients.add(getAverageClusteringCoefficient(graph));
            }

            Row dataRow = sheet.createRow(i + 1);
            dataRow.createCell(0).setCellValue("N=" + currentNumberOfAgents);
            dataRow.createCell(1).setCellValue("p=" + currentLinkProbability);
            dataRow.createCell(2).setCellValue(averageDegrees.mean());
            dataRow.createCell(3).setCellValue(minimumDegrees.mean());
            dataRow.createCell(4).setCellValue(maximumDegrees.mean());
            dataRow.createCell(5).setCellValue(diameters.mean());
            dataRow.createCell(6).setCellValue(averageDistances.mean());
            dataRow.createCell(7).setCellValue(averageClusteringCoefficients.mean());
        }

        FileOutputStream outputStream = new FileOutputStream(
            "/Users/tobyclemson/Documents/Degree Work/4th Year/MSci Project" +
            "/Results/Anghel Game/graph-analysis.xls");
        workbook.write(outputStream);
        outputStream.close();
    }

    private static double getAverageDegree(Graph<Agent, Friendship> graph) {
        return (2.0 * graph.getEdgeCount()) / graph.getVertexCount();
    }

    private static int getMaximumDegree(Graph<Agent, Friendship> graph) {
        return (int) getDegrees(graph).max();
    }

    private static int getMinimumDegree(Graph<Agent, Friendship> graph) {
        return (int) getDegrees(graph).min();
    }

    private static StaticBin1D getDegrees(Graph<Agent, Friendship> graph) {
        StaticBin1D degrees = new StaticBin1D();

        for(Agent vertex : graph.getVertices()) {
            degrees.add(graph.degree(vertex));
        }

        return degrees;
    }

    private static int getDiameter(Graph<Agent, Friendship> graph) {
        return (int) DistanceStatistics.diameter(graph);
    }

    private static double getAverageDistance(Graph<Agent, Friendship> graph) {
        Transformer<Agent,Double> averageDistanceMaps = DistanceStatistics.averageDistances(graph);

        StaticBin1D averageDistances = new StaticBin1D();

        for(Agent agent : graph.getVertices()) {
            averageDistances.add(1/averageDistanceMaps.transform(agent));
        }

        return averageDistances.mean();
    }

    private static double getAverageClusteringCoefficient(Graph<Agent, Friendship> graph) {
        Map<Agent,Double> clusteringCoefficientMap = Metrics.clusteringCoefficients(graph);
        StaticBin1D clusteringCoefficients = new StaticBin1D();

        for(Agent agent : graph.getVertices()) {
            clusteringCoefficients.add(clusteringCoefficientMap.get(agent));
        }

        return clusteringCoefficients.mean();
    }
}
