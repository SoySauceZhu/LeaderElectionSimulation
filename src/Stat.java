import entity.common.Node;
import entity.common.NodeType;
import service.SimulationService;
import util.GenerateNodes;
import util.PrintBox;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Stat {
    private long runtime;
    private int messageCount;
    private long memoryUsage;
    private int leaderCount;

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

    public void runMultipleSimulations() throws CloneNotSupportedException, IOException {
        int[] nodeCounts = {10, 100, 1000};
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

                        results.add(result);
                    }
                }
            }
        }

        StringBuilder json = new StringBuilder();
        json.append("["); // Start JSON array

        for (int i = 0; i < results.size(); i++) {
            json.append("\"").append(results.get(i)).append("\""); // Add string with quotes
            if (i < results.size() - 1) {
                json.append(","); // Add comma for separation
            }
        }

        json.append("]"); // End JSON array

        // Write to file
        try (FileWriter file = new FileWriter("output.json")) {
            file.write(json.toString());
            System.out.println("JSON file created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        Stat stat = new Stat();
        stat.runMultipleSimulations();
    }
}