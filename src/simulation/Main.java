package simulation;

import java.io.IOException;
import java.util.ArrayList;
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
            } else {
                Logger.setLevel(Level.OFF);
            }
            runPerformanceEvaluator(numberOfNodes, clockwise, random, nodeType);
        } else {
//            System.out.println("Choose an option:");
//            System.out.println("1. Interactive Menu");
//            System.out.println("2. Performance Evaluator");
//            int option = sc.nextInt();
//
//            if (option == 1) {
//                interactiveMenu();
//            } else if (option == 2) {
//                performanceEvaluator();
//            } else {
//                System.out.println("Invalid option");
//            }

            interactiveMenu();
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


        showFirstTenNodes(nodes);


        Stat stat = new Stat();
        stat.runSimulation(nodes, msgLog, nodeLog);
        stat.printStats();
        System.out.println("\nDo you want to show final status of nodes? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals("y")) {
            System.out.println("\n");
            PrintBox.printInBox("nodes after simulation");
            for (Node node : nodes) {
                System.out.println(node);
            }
        }
    }


    private static void showFirstTenNodes(List<Node> nodes) throws IOException {
        System.out.println("\n");
        PrintBox.printInBox("nodes after initialization");
        for (int i = 0; i < Math.min(5, nodes.size()); i++) {
            System.out.println(nodes.get(i));
        }
        System.out.println("...");
        System.out.println("\nPress any key to continue...");
        System.in.read();
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

        runPerformanceEvaluator(numberOfNodes, clockwise, random, nodeType);
    }

    private static void runPerformanceEvaluator(int numberOfNodes, boolean clockwise, boolean random, String nodeType) throws CloneNotSupportedException, IOException {
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

        Stat stat = new Stat();
        stat.runSimulation(nodes, false, false);
        stat.printStats();
    }
}