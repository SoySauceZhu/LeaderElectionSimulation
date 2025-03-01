import entity.common.Node;
import service.HS_SimulationService;
import service.LCR_SimulationService;
import service.SimulationService;
import util.GenerateNodes;
import util.PrintBox;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static public Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws CloneNotSupportedException, IOException {

        System.out.println("Select the desired service:");
        System.out.println("1). Lehann-Chang Robots (LCR)");
        System.out.println("2). Hirschberg–Sinclair (HS)");
        int serviceChoice = sc.nextInt();
        int LCRNodeListChoice = -1;
        int HSNodeListChoice = -1;
        int seed;

        switch (serviceChoice) {
            case 1:
                System.out.println("Select the desired node list to generate:");
                System.out.println("1). Sorted LCR Nodes");
                System.out.println("2). Random LCR Nodes");
                LCRNodeListChoice = sc.nextInt();
                break;
            case 2:
                System.out.println("Select the desired node list to generate:");
                System.out.println("1). Sorted HS Nodes");
                System.out.println("2). Random HS Nodes");
                HSNodeListChoice = sc.nextInt();
                break;
            default:
                throw new IllegalArgumentException("Invalid input");
        }

//        if (LCRNodeListChoice == 2 || HSNodeListChoice == 2) {
//            System.out.println("Select random root: (empty or customized)");
//            seed = sc.next
//        }


        System.out.println("Enter the size of nodes:");
        int size = sc.nextInt();

        GenerateNodes generateNodes = new GenerateNodes(42);
        SimulationService service;
        if (serviceChoice == 1) {
            service = new LCR_SimulationService();
        } else {
            service = new HS_SimulationService();
        }

        List<Node> nodes = new ArrayList<>();

        if (serviceChoice == 1) {
            switch (LCRNodeListChoice) {
                case 1:
                    nodes = generateNodes.generateLCRNodes(size);
                    break;
                case 2:
                    nodes = generateNodes.generateRandomLCRNodes(size);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid input");
            }
        }

        if (serviceChoice == 2) {
            switch (HSNodeListChoice) {
                case 1:
                    nodes = generateNodes.generateHSNodes(size);
                    break;
                case 2:
                    nodes = generateNodes.generateRandomHSNodes(size);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid input");
            }
        }

        PrintBox.printInBox(size + " nodes have been created");

        for (int i = 0; i < nodes.size(); i++) {
            if (i == 5) {
                System.out.println("...");
                break;
            }
            System.out.println(nodes.get(i));
        }

        System.out.println("\nPress any key to continue:");

        System.in.read();

        service.setNodes(nodes);
        service.startSimulation();
    }
}
