import service.HS_SimulationService;
import service.LCR_SimulationService;
import service.RandomNodes;
import service.SimulationService;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
    }

    private static void runLCR() {
        Collection<entity.lcr.Node> nodes = RandomNodes.generateRandomNodes(10);
//        Collection<entity.lcr.Node> nodes = RandomNodes.generateNodes(10);
        System.out.println("Nodes generated:");
        for (entity.lcr.Node node : nodes) {
            System.out.println(node);
        }

        System.out.println("\n\n");

        System.out.println("Starting LCR Election Service...");
        SimulationService service = new LCR_SimulationService(nodes);
        service.startSimulation();

        for (entity.lcr.Node node : nodes) {
            System.out.println(node);
        }
    }

}