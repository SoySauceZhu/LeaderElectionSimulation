package service;

import entity.common.Node;
import entity.common.NodeType;
import util.GenerateNodes;
import util.PrintBox;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class PerformanceEvaluator {

    public static void evaluate(int numberOfNodes, boolean clockwise, boolean random, String nodeType) throws CloneNotSupportedException, IOException {
        GenerateNodes generateNodes = new GenerateNodes();
        List<Node> nodes;

        switch (nodeType) {
            case "LCR":
                nodes = random ? generateNodes.generateRandomLCRNodes(numberOfNodes) : generateNodes.generateLCRNodes(numberOfNodes, clockwise);
                break;
            case "HS":
                nodes = random ? generateNodes.generateRandomHSNodes(numberOfNodes) : generateNodes.generateHSNodes(numberOfNodes, clockwise);
                break;
            default:
                throw new IllegalArgumentException("Invalid node type");
        }

        SimulationService service = new SimulationService();
        service.setNodes(nodes);

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();

        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        service.startSimulation(false, false);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterUsedMem - beforeUsedMem;

        int leaderCount = numberOfLeader(nodes);

        PrintBox.printInBox("Performance Evaluation");
        System.out.println("Node Type: " + nodeType);
        System.out.println("Number of Nodes: " + numberOfNodes);
        System.out.println("Clockwise: " + clockwise);
        System.out.println("Random: " + random);
        System.out.println("Execution time: " + (double) duration / (double) 1_000_000 + " milliseconds");
        System.out.println("Memory used: " + memoryUsed + " bytes");
        System.out.println("Number of leaders: " + leaderCount);
        System.out.println("Single leader: " + (leaderCount == 1));
    }

    private static int numberOfLeader(Collection<Node> nodes) {
        int leaderNum = 0;
        for (Node node : nodes) {
            if (node.getNodeType().equals(NodeType.LEADER)) {
                leaderNum++;
            }
        }
        return leaderNum;
    }

    public static void main(String[] args) throws CloneNotSupportedException, IOException {
        PerformanceEvaluator.evaluate(10, true, false, "LCR");
    }
}