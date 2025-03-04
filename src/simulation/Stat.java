package simulation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Stat {
    private long runtime;
    private int messageCount;
    private long memoryUsage;
    private int leaderCount;
    private int roundNumber;

    public void runSimulation(Collection<Node> nodes, boolean msgLog, boolean nodeLog) throws CloneNotSupportedException {
        SimulationService service = new SimulationService();
        service.setNodes(nodes);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();

        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        service.startSimulation(msgLog, nodeLog);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterUsedMem - beforeUsedMem;

        this.runtime = duration;
        this.memoryUsage = memoryUsed;
        this.messageCount = service.getTotalMessages();
        this.leaderCount = numberOfLeader(nodes);
        this.roundNumber = service.getRound();
    }

    private int numberOfLeader(Collection<Node> nodes) {
        int leaderNum = 0;
        for (Node node : nodes) {
            if (node.getNodeType().equals(NodeType.LEADER)) {
                leaderNum++;
            }
        }
        return leaderNum;
    }

    public void printStats() {
        PrintBox.printInBox("Simulation Statistics");
        System.out.println("Execution time: " + (double) runtime / 1_000_000 + " milliseconds");
        System.out.println("Memory used: " + memoryUsage + " bytes");
        System.out.println("Number of messages: " + messageCount);
        System.out.println("Number of leaders: " + leaderCount);
        System.out.println("Number of rounds: " + roundNumber);
    }

    public long getRuntime() {
        return runtime;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public int getLeaderCount() {
        return leaderCount;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void runMultipleSimulations() throws CloneNotSupportedException, IOException {
        int[] nodeCounts = java.util.stream.IntStream.rangeClosed(0, 2000).filter(n -> n % 50 == 0).toArray();
        nodeCounts[0] = 10;
//        int[] nodeCounts = {10, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500, 550, 600, 650, 700, 750, 800, 850, 900, 950, 1000};
        String[] nodeTypes = {"LCR", "HS"};
        boolean[] directions = {true, false}; // true for clockwise, false for counter-clockwise
        boolean[] randoms = {true, false}; // true for random, false for ordered

        List<Map<String, Object>> results = new ArrayList<>();



        GenerateNodes generateNodes = new GenerateNodes();

        for (int count : nodeCounts) {
            for (String type : nodeTypes) {
                for (boolean direction : directions) {
                    for (boolean random : randoms) {
                        List<Node> nodes;
                        if (random) {
                            nodes = type.equals("LCR") ? generateNodes.generateRandomLCRNodes(count) : generateNodes.generateRandomHSNodes(count);
                        } else {
                            nodes = type.equals("LCR") ? generateNodes.generateLCRNodes(count, direction) : generateNodes.generateHSNodes(count, direction);
                        }

                        runSimulation(nodes, false, false);

                        Map<String, Object> result = new HashMap<>();
                        result.put("nodeCount", count);
                        result.put("nodeType", type);
                        result.put("direction", direction ? "clockwise" : "counter-clockwise");
                        result.put("random", random);
                        result.put("runtime", getRuntime());
                        result.put("messageCount", getMessageCount());
                        result.put("memoryUsage", getMemoryUsage());
                        result.put("leaderCount", getLeaderCount());
                        result.put("roundNumber", getRoundNumber());

                        results.add(result);
                    }
                }
            }
        }

        writeJsonToFile(results, "output.json");
    }



    public static void writeJsonToFile(List<Map<String, Object>> data, String filePath) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < data.size(); i++) {
            json.append("  {");
            Map<String, Object> map = data.get(i);
            int j = 0;

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                json.append("\"").append(entry.getKey()).append("\": ");

                if (entry.getValue() instanceof String) {
                    json.append("\"").append(entry.getValue()).append("\"");
                } else {
                    json.append(entry.getValue());
                }

                if (++j < map.size()) {
                    json.append(", ");
                }
            }

            json.append("}");
            if (i < data.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("]");

        // Write JSON string to file
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
            System.out.println("JSON file written successfully to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        Stat stat = new Stat();
        stat.runMultipleSimulations();
    }
}