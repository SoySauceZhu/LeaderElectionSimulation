import entity.common.Node;
import service.HS_SimulationService;
import service.LCR_SimulationService;
import service.SimulationService;
import util.GenerateNodes;

import java.util.List;
import java.util.Scanner;

public class Main {
    static public Scanner sc = new Scanner(System.in);


    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("Select the desired service:");
        System.out.println("1. LCR");
        System.out.println("2. HS");
        int serviceChoice = sc.nextInt();

        System.out.println("Select the desired node list:");
        System.out.println("1. LCR");
        System.out.println("2. HS");
        System.out.println("3. RandomLCR");
        System.out.println("4. RandomHS");
        int nodeListChoice = sc.nextInt();

        System.out.println("Enter the size of nodes:");
        int size = sc.nextInt();

        SimulationService service;
        if (serviceChoice == 1) {
            service = new LCR_SimulationService(size);
        } else {
            service = new HS_SimulationService(size);
        }

        List<Node> nodes;
        switch (nodeListChoice) {
            case 1:
                nodes = GenerateNodes.generateLCRNodes(size);
                break;
            case 2:
                nodes = GenerateNodes.generateHSNodes(size);
                break;
            case 3:
                nodes = GenerateNodes.generateRandomLCRNodes(size);
                break;
            case 4:
                nodes = GenerateNodes.generateRandomHSNodes(size);
                break;
            default:
                throw new IllegalArgumentException("Invalid node list choice");
        }

        service.setNodes(nodes);
        service.startSimulation();
    }
}
