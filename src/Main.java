import entity.lcr.Node;
import service.LCR_SimulationService;
import service.RandomNodes;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        Collection<Node> nodes = RandomNodes.generateRandomNodes(10);
        System.out.println("Nodes generated:");
        for (Node node : nodes) {
            System.out.println(node);
        }

        System.out.println("\n\n");

        System.out.println("Starting LCR Election Service...");
        LCR_SimulationService lcrElectionService = new LCR_SimulationService(nodes);
        lcrElectionService.startSimulation();

        for (Node node : nodes) {
            System.out.println(node);
        }

    }
}