package msci;

import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.io.*;
import java.util.Properties;
import msci.mg.*;
import msci.mg.agents.*;
import msci.mg.factories.MinorityGameFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

public class InfluenceAndChoiceAttendance {
    private static int propertySetIndex;
    private static final String[][] propertySets = {
            {"201", "20"},
            {"201", "40"},
            {"401", "80"},
            {"601", "60"},
            {"601", "120"},
            {"1001", "0.1"}
        };

    private static final int numberOfSettleTimeSteps = 100000;
    private static final int numberOfSettleMeasurements = 10000;
    private static final int numberOfChoiceAttendanceMeasurements = 50000;
    private static final int numberOfLeadershipStructureMeasurements = 1000;
    private static final int leadershipStructureMeasurementInterval = 100;
    private static final int maximumDegreeToMeasure = 500;
    private static final int numberOfRuns = 20;

    private static final String resultsRoot =
        "/Users/tobyclemson/Documents/Degree Work/4th Year/MSci Project" +
        "/Results/Anghel Game/";

    public static void main(String[] args) throws IOException {
        while(hasMorePropertySets()) {
            processNextPropertySet();
        }
    }

    private static boolean hasMorePropertySets() {
        return propertySetIndex < propertySets.length;
    }

    private static void processNextPropertySet() throws IOException {
        Properties propertySet = popPropertySet();
        Workbook workbook = generateResultsForPropertySet(propertySet);

        saveResultsForPropertySet(workbook, propertySet);
    }

    private static Properties popPropertySet() {
        Properties propertySet;

        propertySet = getPropertySetForCurrentIndex();
        propertySetIndex = getNextPropertySetIndex();

        return propertySet;
    }

    private static Workbook generateResultsForPropertySet(Properties propertySet) {
        double[][] settleMeasurements =
            new double[numberOfRuns][numberOfSettleMeasurements];
        double[][] choiceAttendanceMeasurements =
            new double[numberOfRuns][numberOfChoiceAttendanceMeasurements];
        double[][] leadershipStructureMeasurements =
            new double[numberOfRuns][getNumberOfDegreeRows()];

        for(int r = 0; r < numberOfRuns; r++) {
            MinorityGame minorityGame = MinorityGameFactory.construct(
                propertySet
            );

            for(int s = 0; s < numberOfSettleTimeSteps; s++) {
                minorityGame.stepForward();
                if(stillCollectingSettleData(s)) {
                    settleMeasurements[r][s] = getChoiceAttendance(
                        minorityGame
                    );
                }
            }

            int leadershipStructureMeasurementCount = 0;

            for(int t = 0; t < getNumberOfMeasurementTimesteps(); t++) {
                minorityGame.stepForward();
                if(stillCollectingChoiceAttendanceData(t)) {
                    choiceAttendanceMeasurements[r][t] = getChoiceAttendance(
                        minorityGame
                    );
                }
                if(shouldCollectLeadershipStructureThisTimeStep(t, leadershipStructureMeasurementCount)) {
                    DirectedGraph<Agent, Friendship> leadershipStructure =
                        generateLeadershipGraph(minorityGame);

                    for(Agent agent : leadershipStructure.getVertices()) {
                        int followers = leadershipStructure.inDegree(agent);
                        if(numberOfFollowersIsValid(followers)) {
                            leadershipStructureMeasurements[r][followers] += 1;
                        }
                    }

                    leadershipStructureMeasurementCount += 1;
                }
            }

            for(int i = 0; i < leadershipStructureMeasurements.length; i++) {
                leadershipStructureMeasurements[r][i] /=
                    leadershipStructureMeasurementCount;
            }
        }

        Workbook resultsWorkbook = populateWorkbook(
                settleMeasurements,
                choiceAttendanceMeasurements,
                leadershipStructureMeasurements);

        return resultsWorkbook;
    }

    private static void saveResultsForPropertySet(Workbook workbook, Properties propertySet) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(getFilePathForPropertySet(propertySet));
        workbook.write(outputStream);
        outputStream.close();
    }

    private static Properties getPropertySetForCurrentIndex() {
        Properties propertiesForCurrentIndex = new Properties();

        propertiesForCurrentIndex.setProperty("number-of-agents", propertySets[propertySetIndex][0]);
        propertiesForCurrentIndex.setProperty("agent-type", "networked");
        propertiesForCurrentIndex.setProperty("number-of-strategies-per-agent", "2");
        propertiesForCurrentIndex.setProperty("network-type", "scale-free");
        propertiesForCurrentIndex.setProperty("average-number-of-friends", propertySets[propertySetIndex][1]);
        propertiesForCurrentIndex.setProperty("agent-memory-size", "6");

        return propertiesForCurrentIndex;
    }

    private static int getNextPropertySetIndex() {
        return propertySetIndex + 1;
    }

    private static String getFilePathForPropertySet(Properties propertySet) {
        return new StringBuilder(resultsRoot).append("scale-free-networked-game-")
                .append("N=").append(propertySet.getProperty("number-of-agents"))
                .append("-<k>=").append(propertySet.getProperty("average-number-of-friends"))
                .append(".xls")
                .toString();
    }

    private static Workbook initialiseWorkbook() {
        return new HSSFWorkbook();
    }

    private static Sheet initialiseChoiceAttendanceSheet(Workbook workbook, String sheetName, int numberOfRows) {
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle bottomBorderCellStyle = getBottomBorderCellStyle(workbook);
        CellStyle leftBorderCellStyle = getLeftBorderCellStyle(workbook);
        CellStyle bottomLeftBorderCellStyle = getBottomLeftBorderCellStyle(workbook);
        CellStyle bottomRightBorderCellStyle = getBottomRightBorderCellStyle(workbook);
        CellStyle rightBorderCellStyle = getRightBorderCellStyle(workbook);

        Row verticalHeaderRow = sheet.createRow(0);

        CellStyle timeLabelCellStyle = getVerticalLabelCellStyle(workbook);
        timeLabelCellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);

        Cell timeLabel = verticalHeaderRow.createCell(1);
        timeLabel.setCellValue("time:");
        timeLabel.setCellStyle(timeLabelCellStyle);

        CellStyle otherVerticalLabelCellStyle = getVerticalLabelCellStyle(workbook);
        otherVerticalLabelCellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);

        Cell runAverageLabel = verticalHeaderRow.createCell(numberOfRuns+2);
        runAverageLabel.setCellValue("run mean:");
        runAverageLabel.setCellStyle(otherVerticalLabelCellStyle);

        Cell runStandardDeviationLabel = verticalHeaderRow.createCell(numberOfRuns+3);
        runStandardDeviationLabel.setCellValue("run standard deviation:");
        runStandardDeviationLabel.setCellStyle(otherVerticalLabelCellStyle);

        Cell runVarianceLabel = verticalHeaderRow.createCell(numberOfRuns+4);
        runVarianceLabel.setCellValue("run variance:");
        runVarianceLabel.setCellStyle(otherVerticalLabelCellStyle);

        Cell runSkewnessLabel = verticalHeaderRow.createCell(numberOfRuns+5);
        runSkewnessLabel.setCellValue("run skewness:");
        runSkewnessLabel.setCellStyle(otherVerticalLabelCellStyle);

        Cell runKurtosisLabel = verticalHeaderRow.createCell(numberOfRuns+6);
        runKurtosisLabel.setCellValue("run kurtosis:");
        runKurtosisLabel.setCellStyle(otherVerticalLabelCellStyle);

        Row runLabelRow = sheet.createRow(1);
        Cell runLabelCell = runLabelRow.createCell(0);
        runLabelCell.setCellValue("run:");
        runLabelCell.setCellStyle(bottomBorderCellStyle);

        Row meanRow = sheet.createRow(2);
        Cell meanRowLabel = meanRow.createCell(0);
        meanRowLabel.setCellValue("mean:");

        Row standardDeviationRow = sheet.createRow(3);
        Cell standardDeviationRowLabel = standardDeviationRow.createCell(0);
        standardDeviationRowLabel.setCellValue("standard deviation:");

        Row varianceRow = sheet.createRow(4);
        Cell varianceRowLabel = varianceRow.createCell(0);
        varianceRowLabel.setCellValue("variance:");
        varianceRowLabel.setCellStyle(bottomBorderCellStyle);

        runLabelRow.createCell(1).setCellStyle(bottomRightBorderCellStyle);
        meanRow.createCell(1).setCellStyle(rightBorderCellStyle);
        standardDeviationRow.createCell(1).setCellStyle(rightBorderCellStyle);
        varianceRow.createCell(1).setCellStyle(bottomRightBorderCellStyle);

        for(int i = 0; i < 5; i++) {
            int columnIndex = numberOfRuns + 2 + i;
            runLabelRow.createCell(columnIndex).setCellStyle(bottomLeftBorderCellStyle);
            meanRow.createCell(columnIndex).setCellStyle(leftBorderCellStyle);
            standardDeviationRow.createCell(columnIndex).setCellStyle(leftBorderCellStyle);
            varianceRow.createCell(columnIndex).setCellStyle(bottomLeftBorderCellStyle);
        }

        for(int r = 1; r <= numberOfRuns; r++) {
            Cell runHeaderCell = runLabelRow.createCell(r+1);
            runHeaderCell.setCellValue(r);
            runHeaderCell.setCellStyle(bottomBorderCellStyle);

            CellRangeAddress rangeForFormula = new CellRangeAddress(5, numberOfRows + 4, r+1, r+1);

            Cell mean = meanRow.createCell(r+1);
            mean.setCellFormula("AVERAGE(" + rangeForFormula.formatAsString() + ")");

            Cell standardDeviation = standardDeviationRow.createCell(r+1);
            standardDeviation.setCellFormula("STDEV(" + rangeForFormula.formatAsString() + ")");

            Cell variance = varianceRow.createCell(r+1);
            variance.setCellFormula("VAR(" + rangeForFormula.formatAsString() + ")");
            variance.setCellStyle(bottomBorderCellStyle);
        }

        for(int t = 1; t <= numberOfRows; t++) {
            Row attendanceRow = sheet.createRow(t+4);

            Cell timeLabelCell = attendanceRow.createCell(1);
            timeLabelCell.setCellValue(t);
            timeLabelCell.setCellStyle(rightBorderCellStyle);

            CellRangeAddress rangeForFormulae = new CellRangeAddress(t+4, t+4, 2, numberOfRuns+1);

            Cell runAverage = attendanceRow.createCell(numberOfRuns+2);
            runAverage.setCellFormula("AVERAGE(" + rangeForFormulae.formatAsString() + ")");
            runAverage.setCellStyle(leftBorderCellStyle);

            Cell runStandardDeviation = attendanceRow.createCell(numberOfRuns+3);
            runStandardDeviation.setCellFormula("STDEV(" + rangeForFormulae.formatAsString() + ")");
            runStandardDeviation.setCellStyle(leftBorderCellStyle);

            Cell runVariance = attendanceRow.createCell(numberOfRuns+4);
            runVariance.setCellFormula("VAR(" + rangeForFormulae.formatAsString() + ")");
            runVariance.setCellStyle(leftBorderCellStyle);

            Cell runSkewness = attendanceRow.createCell(numberOfRuns+5);
            runSkewness.setCellFormula("SKEW(" + rangeForFormulae.formatAsString() + ")");
            runSkewness.setCellStyle(leftBorderCellStyle);

            Cell runKurtosis = attendanceRow.createCell(numberOfRuns+6);
            runKurtosis.setCellFormula("KURT(" + rangeForFormulae.formatAsString() + ")");
            runKurtosis.setCellStyle(leftBorderCellStyle);
        }

        sheet.createFreezePane(numberOfRuns+2, 5);

        return sheet;
    }

    private static Sheet initialiseLeadershipStructureSheet(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle bottomBorderCellStyle = getBottomBorderCellStyle(workbook);
        CellStyle leftBorderCellStyle = getLeftBorderCellStyle(workbook);
        CellStyle bottomLeftBorderCellStyle = getBottomLeftBorderCellStyle(workbook);
        CellStyle bottomRightBorderCellStyle = getBottomRightBorderCellStyle(workbook);
        CellStyle rightBorderCellStyle = getRightBorderCellStyle(workbook);

        Row verticalHeadersRow = sheet.createRow(0);

        CellStyle timeLabelCellStyle = getVerticalLabelCellStyle(workbook);
        timeLabelCellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);

        Cell timeLabel = verticalHeadersRow.createCell(1);
        timeLabel.setCellValue("number of followers:");
        timeLabel.setCellStyle(timeLabelCellStyle);

        CellStyle verticalLabelCellStyle = getVerticalLabelCellStyle(workbook);
        verticalLabelCellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);

        Cell degreeCountAverageLabel = verticalHeadersRow.createCell(numberOfRuns+2);
        degreeCountAverageLabel.setCellValue("average:");
        degreeCountAverageLabel.setCellStyle(verticalLabelCellStyle);

        Cell normalisedDegreeCountAverageLabel = verticalHeadersRow.createCell(numberOfRuns+3);
        normalisedDegreeCountAverageLabel.setCellValue("normalised average:");
        normalisedDegreeCountAverageLabel.setCellStyle(verticalLabelCellStyle);

        Row runLabelRow = sheet.createRow(1);

        Cell runLabel = runLabelRow.createCell(0);
        runLabel.setCellValue("run:");
        runLabel.setCellStyle(bottomBorderCellStyle);

        runLabelRow.createCell(1).setCellStyle(bottomRightBorderCellStyle);

        runLabelRow.createCell(numberOfRuns+2).setCellStyle(bottomLeftBorderCellStyle);
        runLabelRow.createCell(numberOfRuns+3).setCellStyle(bottomLeftBorderCellStyle);

        for(int r = 1; r <= numberOfRuns; r++) {
            Cell runHeaderCell = runLabelRow.createCell(r+1);
            runHeaderCell.setCellValue(r);
            runHeaderCell.setCellStyle(bottomBorderCellStyle);
        }

        for(int n = 0; n <= maximumDegreeToMeasure; n++) {
            Row attendanceRow = sheet.createRow(n+2);

            Cell attendanceHeaderCell = attendanceRow.createCell(1);
            attendanceHeaderCell.setCellValue(n);
            attendanceHeaderCell.setCellStyle(rightBorderCellStyle);
            
            CellRangeAddress degreeRange = new CellRangeAddress(n+2, n+2, 2, numberOfRuns+1);

            Cell degreeCountAverage = attendanceRow.createCell(numberOfRuns+2);
            degreeCountAverage.setCellFormula("AVERAGE(" + degreeRange.formatAsString() + ")");
            degreeCountAverage.setCellStyle(leftBorderCellStyle);

            CellReference followerCountReference = new CellReference(n+2, numberOfRuns+2);
            CellReference normalisationCountReference = new CellReference(3, numberOfRuns+2, true, true);

            Cell normalisedDegreeCountAverage = attendanceRow.createCell(numberOfRuns+3);
            String countAverageFormula = new StringBuilder()
                    .append(followerCountReference.formatAsString())
                    .append("/")
                    .append(normalisationCountReference.formatAsString())
                    .toString();
            normalisedDegreeCountAverage.setCellFormula(countAverageFormula);
            normalisedDegreeCountAverage.setCellStyle(leftBorderCellStyle);
        }

        sheet.createFreezePane(numberOfRuns + 2, 2);

        return sheet;
    }

    private static int getNumberOfMeasurementTimesteps() {
        return Math.max(
            numberOfChoiceAttendanceMeasurements,
            (numberOfLeadershipStructureMeasurements *
            leadershipStructureMeasurementInterval));
    }

    private static int getNumberOfDegreeRows() {
        return maximumDegreeToMeasure + 1;
    }

    private static boolean stillCollectingSettleData(int s) {
        return s < numberOfSettleMeasurements;
    }

    private static double getChoiceAttendance(MinorityGame minorityGame) {
        return minorityGame.getCommunity().getChoiceTotals().get(Choice.A);
    }

    private static boolean stillCollectingChoiceAttendanceData(int t) {
        return t < numberOfChoiceAttendanceMeasurements;
    }

    private static boolean shouldCollectLeadershipStructureThisTimeStep(int t, int measurementCount) {
        return stillCollectingLeadershipStructureData(measurementCount) &&
            correctIntervalSinceLastMeasurement(t);
    }

    private static boolean correctIntervalSinceLastMeasurement (int t) {
        return (t % leadershipStructureMeasurementInterval == 0);
    }

    private static boolean stillCollectingLeadershipStructureData(int measurementCount) {
        return measurementCount < numberOfLeadershipStructureMeasurements;
    }

    private static DirectedGraph<Agent, Friendship> generateLeadershipGraph(MinorityGame minorityGame) {
        DirectedGraph<Agent,Friendship> leadershipStructure = new DirectedSparseGraph<Agent, Friendship>();

        for(Agent agent : minorityGame.getAgents()) {
            leadershipStructure.addVertex(agent);
        }

        for(Agent agent : minorityGame.getAgents()) {
            if(!(agent instanceof NetworkedAgent)) {
                continue;
            }

            NetworkedAgent networkedAgent = (NetworkedAgent) agent;

            if(networkedAgent.getBestFriend().equals(networkedAgent)) {
                continue;
            }

            leadershipStructure.addEdge(
                new Friendship(),
                networkedAgent,
                networkedAgent.getBestFriend(),
                EdgeType.DIRECTED
            );
        }

        return leadershipStructure;
    }

    private static boolean numberOfFollowersIsValid(int followers) {
        return followers < getNumberOfDegreeRows();
    }

    private static Workbook populateWorkbook(
            double[][] settleMeasurements,
            double[][] choiceAttendanceMeasurements,
            double[][] leadershipStructureMeasurements) {
        Workbook workbook = initialiseWorkbook();

        Sheet currentChoiceAttendanceSheet = initialiseChoiceAttendanceSheet(
                workbook,
                "Choice Attendance",
                numberOfChoiceAttendanceMeasurements);

        Sheet currentSettleSheet = initialiseChoiceAttendanceSheet(
                workbook,
                "Settling",
                numberOfSettleMeasurements);

        Sheet currentLeadershipStructureSheet = initialiseLeadershipStructureSheet(
                workbook,
                "Leadership Structure");

        for(int c = 0; c < numberOfRuns; c++) {
            for(int r = 0; r < numberOfSettleMeasurements; r++) {
                currentSettleSheet
                        .getRow(getChoiceAttendanceRowIndex(r))
                        .createCell(getChoiceAttendanceColumnIndex(c))
                        .setCellValue(settleMeasurements[c][r]);
            }
            for(int r = 0; r < numberOfChoiceAttendanceMeasurements; r++) {
                currentChoiceAttendanceSheet
                        .getRow(getChoiceAttendanceRowIndex(r))
                        .createCell(getChoiceAttendanceColumnIndex(c))
                        .setCellValue(choiceAttendanceMeasurements[c][r]);
            }
            for(int r = 0; r < getNumberOfDegreeRows(); r++) {
                currentLeadershipStructureSheet
                        .getRow(getLeadershipStructureRowIndex(r))
                        .createCell(getLeadershipStructureColumnIndex(c))
                        .setCellValue(leadershipStructureMeasurements[c][r]);
            }
            currentSettleSheet.autoSizeColumn(getChoiceAttendanceColumnIndex(c));
            currentChoiceAttendanceSheet.autoSizeColumn(getChoiceAttendanceColumnIndex(c));
            currentLeadershipStructureSheet.autoSizeColumn(getLeadershipStructureColumnIndex(c));
        }

        return workbook;
    }

    private static int getChoiceAttendanceRowIndex(int r) {
        return r + 5;
    }

    private static int getChoiceAttendanceColumnIndex(int c) {
        return c + 2;
    }

    private static int getLeadershipStructureRowIndex(int r) {
        return r + 2;
    }

    private static int getLeadershipStructureColumnIndex(int c) {
        return c + 2;
    }

    private static CellStyle getVerticalLabelCellStyle(Workbook workbook) {
        CellStyle verticalLabelCellStyle = workbook.createCellStyle();
        
        verticalLabelCellStyle.setRotation((short) (-90));
        verticalLabelCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        verticalLabelCellStyle.setVerticalAlignment(CellStyle.VERTICAL_TOP);

        return verticalLabelCellStyle;
    }

    private static CellStyle getBottomBorderCellStyle(Workbook workbook) {
        CellStyle bottomBorderStyle = workbook.createCellStyle();

        bottomBorderStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        
        return bottomBorderStyle;
    }

    private static CellStyle getRightBorderCellStyle(Workbook workbook) {
        CellStyle rightBorderStyle = workbook.createCellStyle();

        rightBorderStyle.setBorderRight(CellStyle.BORDER_MEDIUM);

        return rightBorderStyle;
    }

    private static CellStyle getLeftBorderCellStyle(Workbook workbook) {
        CellStyle leftBorderStyle = workbook.createCellStyle();

        leftBorderStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);

        return leftBorderStyle;
    }

    private static CellStyle getBottomLeftBorderCellStyle(Workbook workbook) {
        CellStyle bottomLeftBorderStyle = workbook.createCellStyle();

        bottomLeftBorderStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
        bottomLeftBorderStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);

        return bottomLeftBorderStyle;
    }

    private static CellStyle getBottomRightBorderCellStyle(Workbook workbook) {
        CellStyle bottomAndRightBorderStyle = workbook.createCellStyle();

        bottomAndRightBorderStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
        bottomAndRightBorderStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);

        return bottomAndRightBorderStyle;
    }
}
