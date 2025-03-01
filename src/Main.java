import entity.common.Node;
import service.SimulationService;
import util.GenerateNodes;
import util.Logger;
import util.PrintBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {
    static public Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws CloneNotSupportedException, IOException {

        System.out.println("Choose node list type:");
        System.out.println("1. Ordered LCR nodes");
        System.out.println("2. Random LCR nodes");
        System.out.println("3. Ordered HS nodes");
        System.out.println("4. Random HS nodes");
        int choice = sc.nextInt();

        System.out.println("\nEnter the size of the node list:");
        int size = sc.nextInt();

        GenerateNodes generateNodes = new GenerateNodes();

        boolean msgLog = false;
//        System.out.println("Do you want to record Message Log: (y/n)");
//        msgLog = sc.next().equalsIgnoreCase("y");

        boolean nodeLog = false;
//        System.out.println("Do you want to record Node Log: (y/n)");
//        nodeLog = sc.next().equalsIgnoreCase("y");

        List<Node> nodes = new ArrayList<>();
        switch (choice) {
            case 1:
                nodes = generateNodes.generateLCRNodes(size);
                break;
            case 2:
                nodes = generateNodes.generateRandomLCRNodes(size);
                break;
            case 3:
                nodes = generateNodes.generateHSNodes(size);
                break;
            case 4:
                nodes = generateNodes.generateRandomHSNodes(size);
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }


        System.out.println("\nDo you want do show console log info: (y/n)");
        if (sc.next().equalsIgnoreCase("y")) {
            Logger.setLevel(Level.FINE);
        } else {
            Logger.setLevel(Level.OFF);
        }


        SimulationService service = new SimulationService();
        service.setNodes(nodes);


        PrintBox.printInBox(size + " nodes have been created");
        for (int i = 0; i < nodes.size(); i++) {
            if (i == 5) {
                System.out.println("...");
                break;
            }
            System.out.println(nodes.get(i));
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
        System.out.println("\nExecution time: " + (double) duration / (double) 1_000_000 + " milliseconds");


        long afterUsedMem = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterUsedMem - beforeUsedMem;
        System.out.println("Memory used: " + memoryUsed + " bytes");


        System.out.println("\nDo you want to show final node states: (y/n)");
        if (sc.next().equalsIgnoreCase("y")) {
            service.printNodeStates();
        }


//        System.out.println("\nDo you want to inspect the message log? (y/n):");
//        if (sc.next().equalsIgnoreCase("y")) {
//            service.printMsgLog();
//        }
    }
}
