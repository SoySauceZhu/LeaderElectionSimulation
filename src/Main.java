import entity.common.Node;
import entity.common.NodeType;
import service.PerformanceEvaluator;
import service.SimulationService;
import util.GenerateNodes;
import util.Logger;
import util.PrintBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    static public Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws CloneNotSupportedException, IOException {
        if (args.length >= 4) {
            int numberOfNodes = Integer.parseInt(args[0]);
            boolean clockwise = Boolean.parseBoolean(args[1]);
            boolean random = Boolean.parseBoolean(args[2]);
            String nodeType = args[3];
            boolean log = false;
            if (args.length == 5) {
                log = Boolean.parseBoolean(args[4]);
            }
            if (log) {
                Logger.setLevel(Level.FINE);
            } else  {
                Logger.setLevel(Level.OFF);
            }
            PerformanceEvaluator.evaluate(numberOfNodes, clockwise, random, nodeType);
        } else {
            System.out.println("Choose an option:");
            System.out.println("1. Interactive Menu");
            System.out.println("2. Performance Evaluator");
            int option = sc.nextInt();

            if (option == 1) {
                interactiveMenu();
            } else if (option == 2) {
                performanceEvaluator();
            } else {
                System.out.println("Invalid option");
            }
        }
    }

    private static void interactiveMenu() throws CloneNotSupportedException, IOException {
        System.out.println("Choose node list type:");
        System.out.println("1. Ordered LCR nodes (clockwise)");
        System.out.println("2. Ordered LCR nodes (counter-clockwise)");
        System.out.println("3. Ordered HS nodes (clockwise)");
        System.out.println("4. Ordered HS nodes (counter-clockwise)");
        System.out.println("5. Random LCR nodes");
        System.out.println("6. Random HS nodes");
        int choice = sc.nextInt();

        System.out.println("\nEnter the size of the node list:");
        int size = sc.nextInt();

        GenerateNodes generateNodes = new GenerateNodes();

        boolean msgLog = false;
        boolean nodeLog = false;

        List<Node> nodes = new ArrayList<>();
        switch (choice) {
            case 1:
                nodes = generateNodes.generateLCRNodes(size, true);
                break;
            case 2:
                nodes = generateNodes.generateLCRNodes(size, false);
                break;
            case 3:
                nodes = generateNodes.generateHSNodes(size, true);
                break;
            case 4:
                nodes = generateNodes.generateHSNodes(size, false);
                break;
            case 5:
                nodes = generateNodes.generateRandomLCRNodes(size);
                break;
            case 6:
                nodes = generateNodes.generateRandomHSNodes(size);
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }

        SimulationService service = new SimulationService();
        service.setNodes(nodes);

        PrintBox.printInBox(nodes.size() + " nodes have been created");
        for (int i = 0; i < nodes.size(); i++) {
            if (i == 5) {
                System.out.println("...");
                break;
            }
            System.out.println(nodes.get(i));
        }

        System.out.println("\nDo you want to show console log info: (y/n)");
        if (sc.next().equalsIgnoreCase("y")) {
            Logger.setLevel(Level.FINE);
        } else {
            Logger.setLevel(Level.OFF);
        }

        System.out.println("\nPress any key to continue:\n");
        System.in.read();

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();

        long beforeUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();

        service.startSimulation(msgLog, nodeLog);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterUsedMem - beforeUsedMem;

        System.out.println("\n");
        PrintBox.printInBox(numberOfLeader(nodes) + " leader(s) are elected");

        System.out.println("\n\nExecution time: " + (double) duration / (double) 1_000_000 + " milliseconds");
        System.out.println("Memory used: " + memoryUsed + " bytes");

        System.out.println("\nDo you want to show final node states: (y/n)");
        if (sc.next().equalsIgnoreCase("y")) {
            service.printNodeStates();
        }
    }

    private static void performanceEvaluator() throws CloneNotSupportedException, IOException {
        System.out.println("Enter the number of nodes:");
        int numberOfNodes = sc.nextInt();

        System.out.println("Clockwise? (true/false):");
        boolean clockwise = sc.nextBoolean();

        System.out.println("Random nodes? (true/false):");
        boolean random = sc.nextBoolean();

        System.out.println("Enter node type (LCR/HS):");
        String nodeType = sc.next();

        PerformanceEvaluator.evaluate(numberOfNodes, clockwise, random, nodeType);
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
}
